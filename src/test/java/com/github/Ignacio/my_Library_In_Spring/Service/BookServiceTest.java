package com.github.Ignacio.my_Library_In_Spring.Service;

import com.github.Ignacio.my_Library_In_Spring.DTOs.BookRequest;
import com.github.Ignacio.my_Library_In_Spring.DTOs.BookResponse;
import com.github.Ignacio.my_Library_In_Spring.DTOs.Mapper;
import com.github.Ignacio.my_Library_In_Spring.Entity.Author;
import com.github.Ignacio.my_Library_In_Spring.Entity.Book;
import com.github.Ignacio.my_Library_In_Spring.HandingError.NotBooksAvailableException;
import com.github.Ignacio.my_Library_In_Spring.HandingError.NotFoundException;
import com.github.Ignacio.my_Library_In_Spring.Repository.RepositoryBook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private RepositoryBook repo;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private BookService underTest;

    @Captor
    ArgumentCaptor<Book> captor;

    @Test
    void getAllBooks() {
        //given
        Author author1 = new Author(1L
                , "Gabriel García Márquez"
                ,"Chile"
                ,"Escritora chilena conocida por novelas como 'La casa de los espíritus'.");
        Author author2 = new Author(2L
                , "Jane Austen"
                ,"Argentina"
                ,"Fue un escritor, poeta y ensayista argentino, figura clave de la literatura en lengua española.");

        Book book1 = new Book(
                1L,
                "Fahrenheit 451",
                author1,
                "Ballantine Books",
                "Ciencia Ficción",
                true,
                1953
        );

        Book book2 = new Book(
                2L,
                "Kafka en la orilla",
                author2,
                "Shinchosha",
                "Realismo mágico",
                true,
                2002
        );

        List<Book> list = List.of(book1,book2);

        BookResponse response1 = new BookResponse(book1.getGender()
                ,author1
                ,book1.getTitle()
                ,book1.getId());

        BookResponse response2 = new BookResponse(book2.getGender()
                ,author2
                ,book2.getTitle()
                ,book2.getId());

        //when
        when(repo.findAll()).thenReturn(list);
        when(mapper.toBookResponse(book1)).thenReturn(response1);
        when(mapper.toBookResponse(book2)).thenReturn(response2);

        List<BookResponse> result = underTest.getAllBooks();
        //then

        assertEquals(list.size(),result.size());
        assertThat(response1).usingRecursiveComparison().isEqualTo(result.get(0));
        assertThat(response2).usingRecursiveComparison().isEqualTo(result.get(1));
        verify(repo).findAll();
    }


    @Test
    void getAllBooks_shouldReturnException_whenListIsEmpty(){
        //given
        when(repo.findAll()).thenReturn(Collections.emptyList());
        //then + when
        assertThatThrownBy(() -> underTest.getAllBooks())
                .isInstanceOf(NotBooksAvailableException.class)
                .hasMessageContaining("Book not available Exception");
    }

    @Test
    void getBookByName() {
        //given

        String bookTitle = "Kafka en la orilla";


        Author author = new Author(1L
                , "Gabriel García Márquez"
                , "Chile"
                , "Escritora chilena conocida por novelas como 'La casa de los espíritus'.");

        Book book = new Book(
                1L,
                "Kafka en la orilla",
                author,
                "Shinchosha",
                "Realismo mágico",
                true,
                2002
        );

        BookResponse response = new BookResponse("Realismo mágico"
                , author
                , bookTitle
                , 1L);

        //when
        when(repo.findByTitle(bookTitle)).thenReturn(Optional.of(book));
        when(mapper.toBookResponse(book)).thenReturn(response);

        BookResponse result = underTest.getBookByName(bookTitle);

        //then
        verify(repo).findByTitle(bookTitle);
        verify(mapper).toBookResponse(captor.capture());


        Book bookCapured = captor.getValue();

        assertThat(response).usingRecursiveComparison().isEqualTo(result);
        assertThat(bookCapured).isEqualTo(book);

    }

    @Test
    void getBookByName_shouldReturnException_whenNameNotExist(){
        //given
        String bookTitle = "name";
        when(repo.findByTitle(bookTitle)).thenReturn(Optional.empty());

        //when + that
        assertThatThrownBy(() -> underTest.getBookByName(bookTitle))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book with name: "+bookTitle+" not found");
    }

    @Test
    void getBookById() {
        //given
        Author author = new Author(1L
                , "Gabriel García Márquez"
                , "Chile"
                , "Escritora chilena conocida por novelas como 'La casa de los espíritus'.");

        Book book = new Book(
                1L,
                "Kafka en la orilla",
                author,
                "Shinchosha",
                "Realismo mágico",
                true,
                2002
        );

        BookResponse response = new BookResponse(book.getGender()
                , author
                , book.getTitle()
                , book.getId());

        //when
        when(repo.findById(1L)).thenReturn(Optional.of(book));
        when(mapper.toBookResponse(book)).thenReturn(response);

        BookResponse result = underTest.getBookById(1L);
        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
        verify(mapper).toBookResponse(book);
        verify(repo).findById(1L);
    }

    @Test
    void getBookById_shouldReturnException_whenBookIdNotExist(){
        //given
        when(repo.findById(1L)).thenReturn(Optional.empty());

        //when + then
        assertThatThrownBy(() -> underTest.getBookById(1L)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book with id: "+1L+" not found");
    }

    @Test
    void postBook() {
        //given
        Author author = new Author(1L
                , "Gabriel García Márquez"
                , "Chile"
                , "Escritora chilena conocida por novelas como 'La casa de los espíritus'.");

        Book book = new Book(
                1L,
                "Kafka en la orilla",
                author,
                "Shinchosha",
                "Realismo mágico",
                true,
                2002
        );

        BookResponse response = new BookResponse(book.getGender()
                , author
                , book.getTitle()
                , book.getId());

        BookRequest request = new BookRequest(book.getTitle()
                ,author,book.getGender()
                ,book.getYearOfPublication()
                ,book.getEditorial());

        //when
        when(repo.save(any(Book.class))).thenAnswer(invocation ->
                invocation.getArgument(0));
        when(mapper.toEntityBook(request)).thenReturn(book);
        when(mapper.toBookResponse(book)).thenReturn(response);


        BookResponse result = underTest.postBook(request);
        //then
        verify(mapper).toBookResponse(any());
        verify(mapper).toEntityBook(any());
        verify(repo).save(captor.capture());

        Book bookCapted = captor.getValue();

        assertThat(book).isEqualTo(bookCapted);
        assertThat(response).isEqualTo(result);
    }

    @Test
    void deleteBookById() {
        //given
        Author author = new Author(1L
                , "Gabriel García Márquez"
                , "Chile"
                , "Escritora chilena conocida por novelas como 'La casa de los espíritus'.");

        Book book = new Book(
                1L,
                "Kafka en la orilla",
                author,
                "Shinchosha",
                "Realismo mágico",
                true,
                2002
        );
        //when
        when(repo.findById(1L)).thenReturn(Optional.of(book));

        boolean result = underTest.deleteBookById(1L);
        //then
        verify(repo).findById(1L);
        verify(repo).delete(captor.capture());
        verifyNoMoreInteractions(repo);

        Book bookCapture = captor.getValue();
        assertThat(result).isTrue();
        assertThat(bookCapture).isEqualTo(book);

    }

    @Test
    void deleteBookById_shouldReturnException_whenBookIsNotDeleted(){
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.deleteBookById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book not with id: "+1L+" not deleted");
        verify(repo,never()).delete(any());
    }

    @Test
    void putBookById() {
        //given
        Author author = new Author(1L
                , "Gabriel García Márquez"
                , "Chile"
                , "Escritora chilena conocida por novelas como 'La casa de los espíritus'.");

        Book bookExsisting = new Book(
                1L,
                "Kafka en la orilla",
                author,
                "Shinchosha",
                "Realismo mágico",
                true,
                2002
        );

        Book bookUpdate = new Book(1L,
                "1984", // título distinto
                new Author(2L,
                        "George Orwell",
                        "Reino Unido",
                        "Novelista, ensayista y periodista británico."),
                "Secker & Warburg", // editorial distinta
                "Distopía", // género distinto
                true,
                1949 // año distinto
        );

        BookResponse response = new BookResponse(bookUpdate.getGender()
                , bookUpdate.getAuthor()
                , bookUpdate.getTitle()
                , bookUpdate.getId());

        BookRequest request = new BookRequest(bookUpdate.getTitle()
                ,bookUpdate.getAuthor()
                ,bookUpdate.getGender()
                ,bookUpdate.getYearOfPublication()
                ,bookUpdate.getEditorial());

        //when
        when(repo.findById(1L)).thenReturn(Optional.of(bookExsisting));
        when(mapper.toBookResponse(any(Book.class))).thenReturn(response);
        when(repo.save(any(Book.class))).thenAnswer((param) -> param.getArgument(0,Book.class));

        BookResponse result = underTest.putBookById(1L,request);

        verify(repo).findById(1L);
        verify(mapper).toBookResponse(captor.capture());
        Book captured = captor.getValue();
        assertThat(captured)
                .usingRecursiveComparison()
                .isEqualTo(bookUpdate);
        verify(repo).save(captor.capture());

        Book bookCapture = captor.getValue();

        assertThat(bookCapture).usingRecursiveComparison().isEqualTo(bookExsisting);
        assertThat(response).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void putBookById_shouldReturnException_whenBookIdNotExist(){
        //given
        BookRequest update = new BookRequest();
        when(repo.findById(1L)).thenReturn(Optional.empty());

        //when + then
        assertThatThrownBy(() -> underTest.putBookById(1L,update)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book with id: "+1L+" not found");
    }

    @Test
    void putBookByName() {
        //given
        Author author = new Author(1L
                , "Gabriel García Márquez"
                , "Chile"
                , "Escritora chilena conocida por novelas como 'La casa de los espíritus'.");

        Book bookExsisting = new Book(
                1L,
                "Kafka en la orilla",
                author,
                "Shinchosha",
                "Realismo mágico",
                true,
                2002
        );

        Book bookUpdate = new Book(1L,
                "1984", // título distinto
                new Author(2L,
                        "George Orwell",
                        "Reino Unido",
                        "Novelista, ensayista y periodista británico."),
                "Secker & Warburg", // editorial distinta
                "Distopía", // género distinto
                true,
                1949 // año distinto
        );

        BookResponse response = new BookResponse(bookUpdate.getGender()
                , bookUpdate.getAuthor()
                , bookUpdate.getTitle()
                , bookUpdate.getId());

        BookRequest request = new BookRequest(bookUpdate.getTitle()
                ,bookUpdate.getAuthor()
                ,bookUpdate.getGender()
                ,bookUpdate.getYearOfPublication()
                ,bookUpdate.getEditorial());
        //when
        when(repo.findByTitle("Kafka en la orilla")).thenReturn(Optional.of(bookExsisting));
        when(repo.save(any(Book.class))).thenAnswer(param -> param.getArgument(0,Book.class));
        when(mapper.toBookResponse(any(Book.class))).thenReturn(response);

        BookResponse result = underTest.putBookByName("Kafka en la orilla",request);

        verify(repo).findByTitle("Kafka en la orilla");
        verify(mapper).toBookResponse(captor.capture());
        Book captured = captor.getValue();
        assertThat(captured)
                .usingRecursiveComparison()
                .isEqualTo(bookUpdate);
        verify(repo).save(captor.capture());

        Book bookCapture = captor.getValue();

        assertThat(bookCapture).usingRecursiveComparison().isEqualTo(bookExsisting);
        assertThat(response).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void putBookById_shouldReturnException_whenBookNameNotExist(){
        //given
        BookRequest update = new BookRequest();
        when(repo.findByTitle(any(String.class))).thenReturn(Optional.empty());

        //when + then
        assertThatThrownBy(() -> underTest.putBookByName("Richard",update)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book with name: "+"Richard"+" not found");

    }

    @Test
    void patchBookById() {
        //given
        Author author = new Author(1L
                , "Gabriel García Márquez"
                , "Chile"
                , "Escritora chilena conocida por novelas como 'La casa de los espíritus'.");


        Book bookExsisting = new Book(
                1L,
                "Kafka en la orilla",
                author,
                "Shinchosha",
                "Realismo mágico",
                true,
                2002
        );
        Book bookExpected = new Book(1L,
                "1984", // título distinto
                new Author(2L,
                        "George Orwell",
                        "Reino Unido",
                        "Novelista, ensayista y periodista británico."),
                "Secker & Warburg", // editorial distinta
                "Distopía", // género distinto
                true,
                1949 // año distinto
        );

        BookResponse response = new BookResponse(bookExpected.getGender()
                , bookExpected.getAuthor()
                , bookExpected.getTitle()
                , bookExpected.getId());

        Map<String,Object> request = new HashMap<>();
        request.put("gender","Distopía");
        request.put("author", new Author(2L,
                "George Orwell",
                "Reino Unido",
                "Novelista, ensayista y periodista británico."));

        request.put("title","1984");
        request.put("editorial","Secker & Warburg");
        request.put("yearOfPublication",1949);

        //when
        when(repo.findById(1L)).thenReturn(Optional.of(bookExsisting));
        when(mapper.toBookResponse(any(Book.class))).thenReturn(response);
        when(repo.save(any(Book.class))).thenAnswer(param -> param.getArgument(0));

        BookResponse result = underTest.patchBookById(1L,request);
        //then

        verify(repo).findById(1L);
        verify(mapper).toBookResponse(bookExsisting);

        verify(mapper).toBookResponse(captor.capture());
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(bookExsisting);

        verify(repo).save(captor.capture());
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(bookExpected);
        assertThat(result).isEqualTo(response);
    }

    @Test
    void patchBookById_shouldReturnException_whenBookIdNotExist(){

    }

    @Test
    void patchBookByName() {
        //given
        Author author = new Author(1L
                , "Gabriel García Márquez"
                , "Chile"
                , "Escritora chilena conocida por novelas como 'La casa de los espíritus'.");


        Book bookExsisting = new Book(
                1L,
                "Kafka en la orilla",
                author,
                "Shinchosha",
                "Realismo mágico",
                true,
                2002
        );
        Book bookExpected = new Book(1L,
                "1984", // título distinto
                new Author(2L,
                        "George Orwell",
                        "Reino Unido",
                        "Novelista, ensayista y periodista británico."),
                "Secker & Warburg", // editorial distinta
                "Distopía", // género distinto
                true,
                1949 // año distinto
        );

        BookResponse response = new BookResponse(bookExpected.getGender()
                , bookExpected.getAuthor()
                , bookExpected.getTitle()
                , bookExpected.getId());

        Map<String,Object> request = new HashMap<>();
        request.put("gender","Distopía");
        request.put("author", new Author(2L,
                "George Orwell",
                "Reino Unido",
                "Novelista, ensayista y periodista británico."));

        request.put("title","1984");
        request.put("editorial","Secker & Warburg");
        request.put("yearOfPublication",1949);

        //when
        when(repo.findByTitle("Kafka en la orilla")).thenReturn(Optional.of(bookExsisting));
        when(mapper.toBookResponse(any(Book.class))).thenReturn(response);
        when(repo.save(any(Book.class))).thenAnswer(param -> param.getArgument(0));

        BookResponse result = underTest.patchBookByName("Kafka en la orilla",request);
        //then

        verify(repo).findByTitle("Kafka en la orilla");
        verify(mapper).toBookResponse(bookExsisting);

        verify(mapper).toBookResponse(captor.capture());
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(bookExsisting);

        verify(repo).save(captor.capture());
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(bookExpected);
        assertThat(result).isEqualTo(response);
    }

    @Test
    void patchBookById_shouldReturnException_whenBookNameNotExist(){
        //given
        Map<String,Object> update = new HashMap<>();
        when(repo.findByTitle(any(String.class))).thenReturn(Optional.empty());

        //when + then
        assertThatThrownBy(() -> underTest.patchBookByName("Richard",update)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book with name: "+"Richard"+" not found");
    }
}