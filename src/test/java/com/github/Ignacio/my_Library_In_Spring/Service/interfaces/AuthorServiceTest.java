package com.github.Ignacio.my_Library_In_Spring.Service.interfaces;

import com.github.Ignacio.my_Library_In_Spring.DTOs.AuthorRequest;
import com.github.Ignacio.my_Library_In_Spring.DTOs.AuthorResponse;
import com.github.Ignacio.my_Library_In_Spring.DTOs.Mapper;
import com.github.Ignacio.my_Library_In_Spring.Entity.Author;
import com.github.Ignacio.my_Library_In_Spring.Entity.Book;
import com.github.Ignacio.my_Library_In_Spring.HandingError.NotAuthorAvailableException;
import com.github.Ignacio.my_Library_In_Spring.HandingError.NotBooksAvailableException;
import com.github.Ignacio.my_Library_In_Spring.HandingError.NotFoundException;
import com.github.Ignacio.my_Library_In_Spring.Repository.RepositoryAuthor;
import com.github.Ignacio.my_Library_In_Spring.Service.BookService;
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
class AuthorServiceTest {

    @Mock
    private RepositoryAuthor repo;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private AuthorService underTest;

    @Captor
    ArgumentCaptor<Author> captor;

    private Author author;
    private AuthorRequest request;
    private AuthorResponse expectedOutput;
    private Author authorUpdate;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

         author = new Author(1L
                ,"Original"
                ,"Original nationality"
                ,"Original bio");

        request = new AuthorRequest("Updated"
                ,"Updated nationality"
                ,"Updated bio");

        expectedOutput = new AuthorResponse(1L
                ,"Updated"
                ,"Updated nationality"
                ,"Updated bio");

        authorUpdate = new Author(1L
                ,"Updated"
                ,"Updated nationality"
                ,"Updated bio");
    }

    @Test
    void getAllAuthors() {
        when(repo.findAll()).thenReturn(List.of(author));
        when(mapper.toAuthorResponse(author)).thenReturn(expectedOutput);

        List<AuthorResponse> result = underTest.getAllAuthors();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedOutput, result.get(0));
    }

    @Test
    void getAllAuthors_shouldReturnException_whenListIsEmpty(){
        when(repo.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> underTest.getAllAuthors())
                .isInstanceOf(NotAuthorAvailableException.class)
                .hasMessageContaining("there are not authors available");
    }


    @Test
    void getAuthorById() {
        when(repo.findById(1L)).thenReturn(Optional.of(author));
        when(mapper.toAuthorResponse(author)).thenReturn(expectedOutput);

        AuthorResponse result = underTest.getAuthorById(1L);

        assertNotNull(result);
        verify(repo).findById(1L);
        verify(mapper).toAuthorResponse(author);
        assertThat(result).isEqualTo(expectedOutput);
    }

    @Test
    void getAuthorById_shouldThrowNotFoundException_whenAuthorNotFound() {
        Long id = 1L;
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getAuthorById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Author with id " + id + " not found");
    }

    @Test
    void getAuthorByName() {
        when(repo.findByName("Original")).thenReturn(Optional.of(author));
        when(mapper.toAuthorResponse(author)).thenReturn(expectedOutput);

        AuthorResponse result = underTest.getAuthorByName("Original");

        assertNotNull(result);
        verify(repo).findByName("Original");
        verify(mapper).toAuthorResponse(author);
        assertThat(result).isEqualTo(expectedOutput);
    }

    @Test
    void getAuthorByName_shouldThrowNotFoundException_whenAuthorNotFound() {
        String name = "Unknown";
        when(repo.findByName(name)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getAuthorByName(name))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Author with name " + name + " not found");
    }

    @Test
    void post() {
        when(mapper.toEntityAuthor(request)).thenReturn(author);
        when(mapper.toAuthorResponse(author)).thenReturn(expectedOutput);
        when(repo.save(any(Author.class))).thenAnswer(param -> param.getArgument(0));

        AuthorResponse result = underTest.post(request);

        verify(repo).save(captor.capture());
        verify(mapper).toAuthorResponse(any());
        verify(mapper).toEntityAuthor(any());

        assertThat(captor.getValue()).isEqualTo(author);
        assertThat(expectedOutput).isEqualTo(result);
    }

    @Test
    void putAuthorById() {
        when(repo.findById(1L)).thenReturn(Optional.of(author));
        when(repo.save(any(Author.class))).thenAnswer(param -> param.getArgument(0));
        when(mapper.toAuthorResponse(author)).thenReturn(expectedOutput);

        AuthorResponse result = underTest.putAuthorById(1L,request);

        verify(repo).save(captor.capture());
        verify(repo).findById(1L);
        verify(mapper).toAuthorResponse(any());

        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(authorUpdate);
        assertThat(result).isEqualTo(expectedOutput);
    }

    @Test
    void putAuthorById_shouldThrowNotFoundException_whenAuthorNotFound() {
        Long id = 1L;
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.putAuthorById(id, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Author with id " + id + " not found for update");
    }

    @Test
    void putAuthorByName() {
        when(repo.findByName("Original")).thenReturn(Optional.of(author));
        when(repo.save(any(Author.class))).thenAnswer(param -> param.getArgument(0));
        when(mapper.toAuthorResponse(author)).thenReturn(expectedOutput);

        AuthorResponse result = underTest.putAuthorByName("Original",request);

        verify(repo).save(captor.capture());
        verify(repo).findByName("Original");
        verify(mapper).toAuthorResponse(any());

        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(authorUpdate);
        assertThat(result).isEqualTo(expectedOutput);
    }

    @Test
    void putAuthorByName_shouldThrowNotFoundException_whenAuthorNotFound() {
        String name = "Unknown";
        when(repo.findByName(name)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.putAuthorByName(name, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Author with name " + name + " not found for update");
    }

    @Test
    void patchAuthorById() {
        Map<String,Object> update = new HashMap<>();
        update.put("name","Updated");
        update.put("nationality","Updated nationality");
        update.put("biography","Updated bio");

        when(repo.findById(1L)).thenReturn(Optional.of(author));
        when(mapper.toAuthorResponse(author)).thenReturn(expectedOutput);
        when(repo.save(any(Author.class))).thenAnswer(elem -> elem.getArgument(0));

        AuthorResponse result = underTest.patchAuthorById(1L,update);

        verify(repo).save(captor.capture());
        verify(repo).findById(1L);
        verify(mapper).toAuthorResponse(any());

        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(authorUpdate);
    }

    @Test
    void patchAuthorById_shouldThrowNotFoundException_whenAuthorNotFound() {
        Long id = 1L;
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.patchAuthorById(id, Map.of("name", "NewName")))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Author with id " + id + " not found for update");
    }

    @Test
    void patchAuthorByName() {
        Map<String,Object> update = new HashMap<>();
        update.put("name","Updated");
        update.put("nationality","Updated nationality");
        update.put("biography","Updated bio");

        when(repo.findByName("Original")).thenReturn(Optional.of(author));
        when(mapper.toAuthorResponse(author)).thenReturn(expectedOutput);
        when(repo.save(any(Author.class))).thenAnswer(elem -> elem.getArgument(0));

        AuthorResponse result = underTest.patchAuthorByName("Original",update);

        verify(repo).save(captor.capture());
        verify(repo).findByName("Original");
        verify(mapper).toAuthorResponse(any());

        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(authorUpdate);
    }

    @Test
    void patchAuthorByName_shouldThrowNotFoundException_whenAuthorNotFound() {
        String name = "Unknown";
        when(repo.findByName(name)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.patchAuthorByName(name, Map.of("name", "NewName")))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Author with name " + name + " not found for update");
    }

    @Test
    void deleteAuthorById() {
        when(repo.findById(1L)).thenReturn(Optional.of(author));

        boolean result = underTest.deleteAuthorById(1L);

        verify(repo).delete(captor.capture());
        verify(repo).findById(1L);
        verifyNoMoreInteractions(repo);

        assertThat(captor.getValue()).isEqualTo(author);
        assertThat(result).isTrue();
    }

    @Test
    void deleteAuthorById_shouldThrowNotFoundException_whenAuthorNotFound() {
        Long id = 1L;
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.deleteAuthorById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book not with id: " + id + " not deleted");
    }
}