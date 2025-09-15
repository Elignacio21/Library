package com.github.Ignacio.my_Library_In_Spring.Service;

import com.github.Ignacio.my_Library_In_Spring.DTOs.BookRequest;
import com.github.Ignacio.my_Library_In_Spring.DTOs.BookResponse;
import com.github.Ignacio.my_Library_In_Spring.DTOs.Mapper;
import com.github.Ignacio.my_Library_In_Spring.Entity.Author;
import com.github.Ignacio.my_Library_In_Spring.Entity.Book;
import com.github.Ignacio.my_Library_In_Spring.HandingError.NotBooksAvailableException;
import com.github.Ignacio.my_Library_In_Spring.HandingError.NotFoundException;
import com.github.Ignacio.my_Library_In_Spring.Repository.RepositoryAuthor;
import com.github.Ignacio.my_Library_In_Spring.Repository.RepositoryBook;
import com.github.Ignacio.my_Library_In_Spring.Service.interfaces.BookServiceInterface;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookService implements BookServiceInterface {

    private static Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private RepositoryBook repoBook;

    @Autowired
    private RepositoryAuthor repoAuthor;
    @Autowired
    private Mapper mapper;

    public List<BookResponse> getAllBooks(){
        logger.info("entering the method getAllBooks");
        List<BookResponse> list = repoBook.findAll().stream().map(elem -> mapper.toBookResponse(elem)).toList();
        if(list.isEmpty()){
            logger.warn("there are not books show");
            throw new NotBooksAvailableException("Book not available Exception");
        }
        logger.info("books Retrieved {} :",list.size());
        return list;
    }

    @Override
    public BookResponse getBookByName( String title) {
        logger.info("Searching book with title: {}",title);
        Book target =repoBook.findByTitle(title)
                .orElseThrow(() -> {
                    logger.error("book with name {} not found",title);
                    return new NotFoundException("Book with name: "+title+" not found");
                });
        logger.info("Found book: {}",target.getTitle());
        return mapper.toBookResponse(target);
    }

    @Override
    public BookResponse getBookById(Long id) {
        logger.info("Searching book with id: {}",id);
        Book target = repoBook.findById(id)
                .orElseThrow(()->{
                    logger.error("book with id {} not found",id);
                   return new NotFoundException("Book with id: "+id+" not found");
                });
        logger.info("Found book id: {}",id);
        return mapper.toBookResponse(target);
    }

    @Override
    public BookResponse postBook(BookRequest input) {
        logger.info("Adding new book with title: {}", input.getTitle());

        // 1. Buscar el Author por ID (CRÍTICO)
        Author author = repoAuthor.findById(input.getAuthorId())
                .orElseThrow(() -> new RuntimeException(
                        "Author not found with id: " + input.getAuthorId()
                ));

        logger.debug("Author found: {} - {}", author.getId(), author.getName());

        // 2. Convertir BookRequest a Entity usando el Author encontrado
        Book addBook = mapper.toEntityBook(input,author); // ← Pasar el author

        // 3. Guardar el libro
        logger.debug("Saving new book: {}", addBook.getTitle());
        repoBook.save(addBook);

        // 4. Devolver respuesta
        logger.info("Book saved successfully with ID: {}", addBook.getId());
        return mapper.toBookResponse(addBook);
    }



    @Override
    public boolean deleteBookById(Long id) {
        logger.info("Attempting book with id: {}",id);
        return repoBook.findById(id)
                .map(book ->{
                    repoBook.delete(book);
                    logger.info("Book with id {} deleted successfully",id);
                    return true;
                }).orElseThrow(() -> {
                    logger.error("Book with id {} not found for deletion",id);
                    return new NotFoundException("Book not with id: "+id+" not deleted");
                });
    }


    @Override
    public BookResponse putBookById(Long id,BookRequest update) {
        logger.info("Updating book with id: {}", id);
        Book bookUpdate = repoBook.findById(id)
                .orElseThrow(() -> {
                    logger.error("Book with id {} not found for update",id);
                    return new NotFoundException("Book with id: "+id+" not found");
                });

        bookUpdate.setTitle(update.getTitle());
        bookUpdate.setGender(update.getGender());
        bookUpdate.setYearOfPublication(update.getYearOfPublication());
        bookUpdate.setEditorial(update.getEditorial());

        repoBook.save(bookUpdate);
        logger.info("book with id {} update successfully",id);
        return mapper.toBookResponse(bookUpdate);

    }

    @Override
    public BookResponse putBookByName(String title,BookRequest update) {
        logger.info("Updating book with name: {}", title);
        Book bookUpdate = repoBook.findByTitle(title)
                .orElseThrow(() -> {
                    logger.error("Book with name {} not found for update",title);
                    return new NotFoundException("Book with name: "+title+" not found");
                });
        bookUpdate.setTitle(update.getTitle());
        bookUpdate.setGender(update.getGender());
        bookUpdate.setYearOfPublication(update.getYearOfPublication());
        bookUpdate.setEditorial(update.getEditorial());

        repoBook.save(bookUpdate);
        logger.info("book with name {} update successfully",title);
        return mapper.toBookResponse(bookUpdate);
    }

    private void applyPatch(@Valid Book book,String key,Object value){
        switch (key){
            case "title" : if(value instanceof String){
                book.setTitle((String) value);
            }
                break;

            case "author" : if(value instanceof Author){
                book.setAuthor((Author) value);
            }
                break;

            case "gender" : if(value instanceof String){
                book.setGender((String) value);
            }
                break;

            case "yearOfPublication": if(value instanceof Number){
                book.setYearOfPublication(((Number) value).intValue());
            }
                break;

            case "editorial" : if(value instanceof String){
                book.setEditorial((String) value);
            }

                break;

            default:throw  new IllegalArgumentException("Field "+key +"not found");

        }

    }

    @Override
    public BookResponse patchBookById(Long id, Map<String, Object> input) {
        logger.info("patching book with id : {}",id);
        Book bookUpdate = repoBook.findById(id).map(book -> {
            input.forEach((key,value)->{
                applyPatch(book,key,value);
            });
            return repoBook.save(book);
        }).orElseThrow(() -> {
            logger.error("Book with id {} not found for patching",id);
            return new NotFoundException("Book with id: "+id+" not found");
        });
        logger.info("Book with id {} patching successfully",id);
        return mapper.toBookResponse(bookUpdate);
    }

    @Override
    public BookResponse patchBookByName(String title, Map<String, Object> input) {
        logger.info("patching book with title : {}",title);
        Book bookUpdate = repoBook.findByTitle(title).map(book -> {
            input.forEach((key,value)->{
                applyPatch(book,key,value);
            });
            return repoBook.save(book);
        }).orElseThrow(() -> new NotFoundException("Book with title: "+title+" not found"));
        logger.info("Book with title {} patching successfully",title);
        return mapper.toBookResponse(bookUpdate);
    }

}
