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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
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
    private RepositoryBook repoBook;

    @Mock
    private RepositoryAuthor repoAuthor;
    @Mock
    private Mapper mapper;

    @InjectMocks
    private BookService underTest;

    @Captor
    ArgumentCaptor<Book> captor;

    private Author author;
    private Author author2;
    private Book book;
    private BookResponse response;
    private BookRequest request;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
         author = new Author(1L
                , "Gabriel García Márquez"
                ,"Chile"
                ,"Escritora chilena conocida por novelas como 'La casa de los espíritus'.");

         book = new Book(
                1L,
                "Fahrenheit 451",
                 author,
                "Ballantine Books",
                "Ciencia Ficción",
                true,
                1953
        );

         response = new BookResponse(book.getGender()
                , author
                , book.getTitle()
                , book.getId());


         request = new BookRequest(book.getTitle()
                , author.getId()
                , book.getGender()
                , book.getYearOfPublication()
                , book.getEditorial());
    }

    @Test
    void getAllBooks() {
        Book book2 =  new Book(
                2L,
                "Kafka en la orilla",
                author2,
                "Shinchosha",
                "Realismo mágico",
                true,
                2002
        );
        Author author2 = new Author(2L
                , "Jane Austen"
                ,"Argentina"
                ,"Fue un escritor, poeta y ensayista argentino, figura clave de la literatura en lengua española.");

        BookResponse response2 = new BookResponse(book2.getGender()
                ,author2
                ,book2.getTitle()
                ,book2.getId());

        List<Book>list = List.of(book,book2);
        //when
        when(repoBook.findAll()).thenReturn(list);
        when(mapper.toBookResponse(book)).thenReturn(response);
        when(mapper.toBookResponse(book2)).thenReturn(response2);

        List<BookResponse> result = underTest.getAllBooks();
        //then

        assertEquals(list.size(),result.size());
        assertThat(response).usingRecursiveComparison().isEqualTo(result.get(0));
        assertThat(response2).usingRecursiveComparison().isEqualTo(result.get(1));
        verify(repoBook).findAll();
    }


    @Test
    void getAllBooks_shouldReturnException_whenListIsEmpty(){
        //given
        when(repoBook.findAll()).thenReturn(Collections.emptyList());
        //then + when
        assertThatThrownBy(() -> underTest.getAllBooks())
                .isInstanceOf(NotBooksAvailableException.class)
                .hasMessageContaining("Book not available Exception");
    }

    @Test
    void getBookByName() {
        //given

        String bookTitle = book.getTitle();
        //when
        when(repoBook.findByTitle(bookTitle)).thenReturn(Optional.of(book));
        when(mapper.toBookResponse(book)).thenReturn(response);

        BookResponse result = underTest.getBookByName(bookTitle);

        //then
        verify(repoBook).findByTitle(bookTitle);
        verify(mapper).toBookResponse(captor.capture());


        Book bookCapured = captor.getValue();

        assertThat(response).usingRecursiveComparison().isEqualTo(result);
        assertThat(bookCapured).isEqualTo(book);

    }

    @Test
    void getBookByName_shouldReturnException_whenNameNotExist(){
        //given
        String bookTitle = "name";
        when(repoBook.findByTitle(bookTitle)).thenReturn(Optional.empty());

        //when + that
        assertThatThrownBy(() -> underTest.getBookByName(bookTitle))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book with name: "+bookTitle+" not found");
    }

    @Test
    void getBookById() {
        //when
        when(repoBook.findById(1L)).thenReturn(Optional.of(book));
        when(mapper.toBookResponse(book)).thenReturn(response);

        BookResponse result = underTest.getBookById(1L);
        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
        verify(mapper).toBookResponse(book);
        verify(repoBook).findById(1L);
    }

    @Test
    void getBookById_shouldReturnException_whenBookIdNotExist(){
        //given
        when(repoBook.findById(1L)).thenReturn(Optional.empty());

        //when + then
        assertThatThrownBy(() -> underTest.getBookById(1L)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book with id: "+1L+" not found");
    }

    @Test
    void postBook() {
        //when
        when(repoAuthor.findById(1L)).thenReturn(Optional.of(author));
        when(mapper.toEntityBook(request,author)).thenReturn(book);
        when(mapper.toBookResponse(book)).thenReturn(response);
        when(repoBook.save(any(Book.class))).thenAnswer(invocation ->
                invocation.getArgument(0));


        BookResponse result = underTest.postBook(request);
        //then
        verify(repoAuthor).findById(1L);
        verify(mapper).toBookResponse(book);
        verify(mapper).toEntityBook(request,author);
        verify(repoBook).save(book);
        assertThat(response).isEqualTo(result);
    }

    @Test
    void postBook_shouldThrowExceptionWhenAuthorNotFound() {
        // Given
        when(repoAuthor.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> underTest.postBook(request));
        verify(repoAuthor).findById(1L);
        verifyNoInteractions(mapper, repoBook);
    }

    @Test
    void deleteBookById() {
        //when
        when(repoBook.findById(1L)).thenReturn(Optional.of(book));

        boolean result = underTest.deleteBookById(1L);
        //then
        verify(repoBook).findById(1L);
        verify(repoBook).delete(captor.capture());
        verifyNoMoreInteractions(repoBook);

        Book bookCapture = captor.getValue();
        assertThat(result).isTrue();
        assertThat(bookCapture).isEqualTo(book);

    }

    @Test
    void deleteBookById_shouldReturnException_whenBookIsNotDeleted(){
        when(repoBook.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.deleteBookById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book not with id: "+1L+" not deleted");
        verify(repoBook,never()).delete(any());
    }

    @Test
    void putBookById() {
        when(repoBook.findById(1L)).thenReturn(java.util.Optional.of(book));
        when(repoBook.save(any(Book.class))).thenReturn(book);
        when(mapper.toBookResponse(any(Book.class))).thenReturn(response);

        BookRequest updateRequest = new BookRequest("new title", author.getId(),
                "new gender", 2020, "new editorial");

        BookResponse result = underTest.putBookById(1L, updateRequest);

        verify(repoBook).findById(1L);
        verify(repoBook).save(captor.capture());
        verify(mapper).toBookResponse(any(Book.class));

        Book savedBook = captor.getValue();
        assertEquals("new title", savedBook.getTitle());
        assertEquals("new gender", savedBook.getGender());
        assertEquals(2020, savedBook.getYearOfPublication());
        assertEquals("new editorial", savedBook.getEditorial());
        assertEquals(author, savedBook.getAuthor());

        assertNotNull(result);

    }

    @Test
    void putBookById_shouldReturnException_whenBookIdNotExist(){
        //given
        BookRequest update = new BookRequest();
        when(repoBook.findById(1L)).thenReturn(Optional.empty());

        //when + then
        assertThatThrownBy(() -> underTest.putBookById(1L,update)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book with id: "+1L+" not found");
    }

    @Test
    void putBookByName() {
        when(repoBook.findByTitle("Fahrenheit 451")).thenReturn(java.util.Optional.of(book));
        when(repoBook.save(any(Book.class))).thenReturn(book);
        when(mapper.toBookResponse(any(Book.class))).thenReturn(response);

        BookRequest updateRequest = new BookRequest("new title", author.getId(),
                "new gender", 2020, "new editorial");

        BookResponse result = underTest.putBookByName("Fahrenheit 451", updateRequest);

        verify(repoBook).findByTitle("Fahrenheit 451");
        verify(repoBook).save(captor.capture());
        verify(mapper).toBookResponse(any(Book.class));

        Book savedBook = captor.getValue();
        assertEquals("new title", savedBook.getTitle());
        assertEquals("new gender", savedBook.getGender());
        assertEquals(2020, savedBook.getYearOfPublication());
        assertEquals("new editorial", savedBook.getEditorial());
        assertEquals(author, savedBook.getAuthor());

        assertNotNull(result);
    }

    @Test
    void putBookById_shouldReturnException_whenBookNameNotExist(){
        //given
        String name = "Richard";
        BookRequest update = new BookRequest();
        when(repoBook.findByTitle(any(String.class))).thenReturn(Optional.empty());

        //when + then
        assertThatThrownBy(() -> underTest.putBookByName("Richard",update)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book with name: "+name+" not found");

    }

    @Test
    void patchBookById() {
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
        when(repoBook.findById(1L)).thenReturn(Optional.of(book));
        when(mapper.toBookResponse(any(Book.class))).thenReturn(response);
        when(repoBook.save(any(Book.class))).thenAnswer(param -> param.getArgument(0));

        BookResponse result = underTest.patchBookById(1L,request);
        //then

        verify(repoBook).findById(1L);
        verify(mapper).toBookResponse(book);

        verify(mapper).toBookResponse(captor.capture());
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(book);

        verify(repoBook).save(captor.capture());
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(bookExpected);
        assertThat(result).isEqualTo(response);
    }

    @Test
    void patchBookById_shouldReturnException_whenBookIdNotExist(){

    }

    @Test
    void patchBookByName() {
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
        when(repoBook.findByTitle("Kafka en la orilla")).thenReturn(Optional.of(book));
        when(mapper.toBookResponse(any(Book.class))).thenReturn(response);
        when(repoBook.save(any(Book.class))).thenAnswer(param -> param.getArgument(0));

        BookResponse result = underTest.patchBookByName("Kafka en la orilla",request);
        //then

        verify(repoBook).findByTitle("Kafka en la orilla");
        verify(mapper).toBookResponse(book);

        verify(mapper).toBookResponse(captor.capture());
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(book);

        verify(repoBook).save(captor.capture());
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(bookExpected);
        assertThat(result).isEqualTo(response);
    }

    @Test
    void patchBookById_shouldReturnException_whenBookNameNotExist(){
        //given
        String name = "name";
        Map<String,Object> update = new HashMap<>();
        when(repoBook.findByTitle(any(String.class))).thenReturn(Optional.empty());

        //when + then
        assertThatThrownBy(() -> underTest.patchBookByName(name,update)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book with title: "+name+" not found");
    }
}