package com.github.Ignacio.my_Library_In_Spring.Service;

import com.github.Ignacio.my_Library_In_Spring.DTOs.BookRequest;
import com.github.Ignacio.my_Library_In_Spring.DTOs.BookResponse;
import com.github.Ignacio.my_Library_In_Spring.DTOs.Mapper;
import com.github.Ignacio.my_Library_In_Spring.Entity.Author;
import com.github.Ignacio.my_Library_In_Spring.Entity.Book;
import com.github.Ignacio.my_Library_In_Spring.HandingError.NotBooksAvailableException;
import com.github.Ignacio.my_Library_In_Spring.HandingError.NotFoundException;
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

    private static Logger logeer = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private RepositoryBook repoBook;

    @Autowired
    private Mapper mapper;

    public List<BookResponse> getAllBooks(){
        List<BookResponse> list = repoBook.findAll().stream().map(elem -> mapper.toBookResponse(elem)).toList();
        if(list.isEmpty()){
            throw new NotBooksAvailableException("Book not available Exception");
        }
        return list;
    }

    @Override
    public BookResponse getBookByName( String name) {

        Book target =repoBook.findByTitle(name)
                .orElseThrow(() -> new NotFoundException("Book with name: "+name+" not found"));

        return mapper.toBookResponse(target);
    }

    @Override
    public BookResponse getBookById(Long id) {
        Book target = repoBook.findById(id)
                .orElseThrow(()-> new NotFoundException("Book with id: "+id+" not found"));

        return mapper.toBookResponse(target);
    }

    @Override
    public BookResponse postBook(BookRequest input) {
        Book addBook = mapper.toEntityBook(input);

        repoBook.save(addBook);

        return mapper.toBookResponse(addBook);
    }



    @Override
    public boolean deleteBookById(Long id) {
        return repoBook.findById(id)
                .map(book ->{
                    repoBook.delete(book);
                    return true;
                }).orElseThrow(() -> new NotFoundException("Book not with id: "+id+" not deleted"));
    }


    @Override
    public BookResponse putBookById(Long id,BookRequest update) {
        Book bookUpdate = repoBook.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id: "+id+" not found"));

        bookUpdate.setTitle(update.getTitle());
        bookUpdate.setAuthor(update.getAuthor());
        bookUpdate.setGender(update.getGender());
        bookUpdate.setYearOfPublication(update.getYearOfPublication());
        bookUpdate.setEditorial(update.getEditorial());

        repoBook.save(bookUpdate);
        return mapper.toBookResponse(bookUpdate);

    }

    @Override
    public BookResponse putBookByName(String name,BookRequest update) {
        Book bookUpdate = repoBook.findByTitle(name)
                .orElseThrow(() -> new NotFoundException("Book with name: "+name+" not found"));

        bookUpdate.setTitle(update.getTitle());
        bookUpdate.setAuthor(update.getAuthor());
        bookUpdate.setGender(update.getGender());
        bookUpdate.setYearOfPublication(update.getYearOfPublication());
        bookUpdate.setEditorial(update.getEditorial());

        repoBook.save(bookUpdate);
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
        Book bookUpdate = repoBook.findById(id).map(book -> {
            input.forEach((key,value)->{
                applyPatch(book,key,value);
            });
            return repoBook.save(book);
        }).orElseThrow(() -> new NotFoundException("Book with id: "+id+" not found"));

        return mapper.toBookResponse(bookUpdate);
    }

    @Override
    public BookResponse patchBookByName(String name, Map<String, Object> input) {
        Book bookUpdate = repoBook.findByTitle(name).map(book -> {
            input.forEach((key,value)->{
                applyPatch(book,key,value);
            });
            return repoBook.save(book);
        }).orElseThrow(() -> new NotFoundException("Book with name: "+name+" not found"));

        return mapper.toBookResponse(bookUpdate);
    }

}
