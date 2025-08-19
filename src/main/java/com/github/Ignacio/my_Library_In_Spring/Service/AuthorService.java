package com.github.Ignacio.my_Library_In_Spring.Service;

import com.github.Ignacio.my_Library_In_Spring.DTOs.AuthorRequest;
import com.github.Ignacio.my_Library_In_Spring.DTOs.AuthorResponse;
import com.github.Ignacio.my_Library_In_Spring.DTOs.Mapper;
import com.github.Ignacio.my_Library_In_Spring.Entity.Author;
import com.github.Ignacio.my_Library_In_Spring.HandingError.NotAuthorAvailableException;
import com.github.Ignacio.my_Library_In_Spring.HandingError.NotFoundException;
import com.github.Ignacio.my_Library_In_Spring.Repository.RepositoryAuthor;
import com.github.Ignacio.my_Library_In_Spring.Service.interfaces.AuthorServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthorService implements AuthorServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    @Autowired
    private RepositoryAuthor repoAuthor;

    @Autowired
    private Mapper mapper;

    @Override
    public List<AuthorResponse> getAllAuthors(){
        logger.info("entering to method getAllAuthors");
        List<AuthorResponse> list = repoAuthor.findAll()
                .stream()
                .map(elem -> mapper.toAuthorResponse(elem)).toList();

        if(list.isEmpty()){
            logger.warn("there are not books show");
            throw new NotAuthorAvailableException("there are not authors available");
        }
        logger.info("authors Retrieved: {} ",list);
        return list;
    }

    @Override
    public AuthorResponse getAuthorById(Long id) {
        logger.info("entering to method getAuthorById with id: {}",id);
        Author target = repoAuthor.findById(id).orElseThrow(
                () -> {
                    logger.error("author with id: {} not found",id);
                    return new NotFoundException("Author with id "+id+ " not found");
                });

        logger.info("author found: {}",target.getName());
        return mapper.toAuthorResponse(target);
    }

    @Override
    public AuthorResponse getAuthorByName(String name) {
        logger.info("entering to method getAuthorByName with name: {}",name);
        Author target = repoAuthor.findByName(name).orElseThrow(
                () -> {
                    logger.error("author with name: {} not found",name);
                    return new NotFoundException("Author with name "+name+ " not found");
                });
        logger.info("author found : {}",target.getName());
        return mapper.toAuthorResponse(target);
    }

    @Override
    public AuthorResponse post(AuthorRequest request) {
        logger.info("entering to method post");
        Author input = mapper.toEntityAuthor(request);
        logger.debug("save new author with name: {}",input.getName());
        repoAuthor.save(input);
        return  mapper.toAuthorResponse(input);

    }

    @Override
    public AuthorResponse putAuthorById(Long id,AuthorRequest request) {
        logger.info("entering to method putAuthorById with id: {}",id);
        Author target = repoAuthor.findById(id).orElseThrow(
                ()->{
                    logger.error("author with id {} not found",id);
                    return new NotFoundException("Author with id "+id+" not found for update");
                } );

        target.setBiography(request.getBiography());
        target.setName(request.getName());
        target.setNationality(request.getNationality());

        repoAuthor.save(target);
        logger.info("author with id {} update successfully",id);
        return mapper.toAuthorResponse(target);
    }

    @Override
    public AuthorResponse putAuthorByName(String name,AuthorRequest request) {
        logger.info("entering to method putAuthorByName with name {}",name);
        Author target = repoAuthor.findByName(name).orElseThrow(
                ()->{
                    logger.error("author with name {} not found",name);
                    return new NotFoundException("Author with name "+name+" not found for update");
                } );

        target.setBiography(request.getBiography());
        target.setName(request.getName());
        target.setNationality(request.getNationality());

        repoAuthor.save(target);
        logger.info("author with name {} update successfully",name);
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
        logger.info("entering method patchAuthorById with id {}",id);
        Author target = repoAuthor.findById(id).map(author -> {
            updates.forEach((key,value) -> {
                applyPatch(author,key,value);
            });
            return repoAuthor.save(author);
        }).orElseThrow(()-> {
            logger.error("author with id {} not found for parching",id);
            return new NotFoundException("Author with id "+id+" not found for update");
        });
        logger.info("author with id {} patching successfully",id);
        return mapper.toAuthorResponse(target);
    }

    @Override
    public AuthorResponse patchAuthorByName(String name,Map<String,Object> updates) {
        logger.info("entering to method patchAuthorByName with name {}",name);
        Author target = repoAuthor.findByName(name).map(author -> {
            updates.forEach((key,value) -> {
                applyPatch(author,key,value);
            });
            return repoAuthor.save(author);
        }).orElseThrow(()-> {
            logger.error("author with name {} not found for patching",name);
            return new NotFoundException("Author with name "+name+" not found for update");
        });
        logger.info("author with name {} patching successfully",name);
        return mapper.toAuthorResponse(target);
    }

    @Override
    public boolean deleteAuthorById(Long id) {
        logger.info("entering method deleteAuthorById with id {}",id);
        return repoAuthor.findById(id).map(author -> {
            repoAuthor.delete(author);
            logger.info("author with id {} deleted successfully",id);
            return true;
        }).orElseThrow(() -> {
            logger.error("author with id {} not found for delete",id);
            return new NotFoundException("Book not with id: "+id+" not deleted");
        });
    }
}
