package com.github.Ignacio.my_Library_In_Spring.Service.interfaces;

import com.github.Ignacio.my_Library_In_Spring.DTOs.AuthorRequest;
import com.github.Ignacio.my_Library_In_Spring.DTOs.AuthorResponse;
import com.github.Ignacio.my_Library_In_Spring.DTOs.Mapper;
import com.github.Ignacio.my_Library_In_Spring.Entity.Author;
import com.github.Ignacio.my_Library_In_Spring.HandingError.NotAuthorAvailableException;
import com.github.Ignacio.my_Library_In_Spring.HandingError.NotFoundException;
import com.github.Ignacio.my_Library_In_Spring.Repository.RepositoryAuthor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthorService implements AuthorServiceInterface{

    private static Logger logger = LoggerFactory.getLogger(AuthorService.class);

    @Autowired
    private RepositoryAuthor repoAuthor;

    @Autowired
    private Mapper mapper;

    @Override
    public List<AuthorResponse> getAllAuthors(){
        List<AuthorResponse> list = repoAuthor.findAll()
                .stream()
                .map(elem -> mapper.toAuthorResponse(elem)).toList();

        if(list.isEmpty()){
            throw new NotAuthorAvailableException("there are not authors available");
        }

        return list;
    }

    @Override
    public AuthorResponse getAuthorById(Long id) {
        Author target = repoAuthor.findById(id).orElseThrow(
                () -> new NotFoundException("Author with id "+id+ " not found"));

        return mapper.toAuthorResponse(target);
    }

    @Override
    public AuthorResponse getAuthorByName(String name) {
        Author target = repoAuthor.findByName(name).orElseThrow(
                () -> new NotFoundException("Author with name "+name+ " not found"));

        return mapper.toAuthorResponse(target);
    }

    @Override
    public AuthorResponse post(AuthorRequest request) {
        Author input = mapper.toEntityAuthor(request);
        repoAuthor.save(input);
        return  mapper.toAuthorResponse(input);

    }

    @Override
    public AuthorResponse putAuthorById(Long id,AuthorRequest request) {
        Author target = repoAuthor.findById(id).orElseThrow(
                ()->new NotFoundException("Author with id "+id+" not found for update") );

        target.setBiography(request.getBiography());
        target.setName(request.getName());
        target.setNationality(request.getNationality());

        repoAuthor.save(target);
        return mapper.toAuthorResponse(target);
    }

    @Override
    public AuthorResponse putAuthorByName(String name,AuthorRequest request) {
        Author target = repoAuthor.findByName(name).orElseThrow(
                ()->new NotFoundException("Author with name "+name+" not found for update") );

        target.setBiography(request.getBiography());
        target.setName(request.getName());
        target.setNationality(request.getNationality());

        repoAuthor.save(target);
        return mapper.toAuthorResponse(target);
    }

    private void applyPatch(Author author,String key,Object value){
        switch (key){
            case "name" : if(value instanceof String){
                author.setName((String) value);
            }break;

            case "biography": if (value instanceof String){
                author.setBiography((String) value);
            }break;

            case "nationality": if(value instanceof String){
                author.setNationality((String) value);
            }break;

            default: throw  new IllegalArgumentException("Field "+key +"not found");
        }
    }



    @Override
    public AuthorResponse patchAuthorById(Long id, Map<String,Object> updates) {
        Author target = repoAuthor.findById(id).map(author -> {
            updates.forEach((key,value) -> {
                applyPatch(author,key,value);
            });
            return repoAuthor.save(author);
        }).orElseThrow(()-> new NotFoundException("Author with id "+id+" not found for update"));

        return mapper.toAuthorResponse(target);
    }

    @Override
    public AuthorResponse patchAuthorByName(String name,Map<String,Object> updates) {
        Author target = repoAuthor.findByName(name).map(author -> {
            updates.forEach((key,value) -> {
                applyPatch(author,key,value);
            });
            return repoAuthor.save(author);
        }).orElseThrow(()-> new NotFoundException("Author with name "+name+" not found for update"));

        return mapper.toAuthorResponse(target);
    }

    @Override
    public boolean deleteAuthorById(Long id) {
        return repoAuthor.findById(id).map(author -> {
            repoAuthor.delete(author);
            return true;
        }).orElseThrow(() -> {
            return new NotFoundException("Book not with id: "+id+" not deleted");
        });
    }
}
