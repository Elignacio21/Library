package com.github.Ignacio.my_Library_In_Spring.Controllers;

import com.github.Ignacio.my_Library_In_Spring.DTOs.BookRequest;
import com.github.Ignacio.my_Library_In_Spring.DTOs.BookResponse;
import com.github.Ignacio.my_Library_In_Spring.Service.BookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/book")
public class BookController {

    private static Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService service;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllBooks(){
        try{
            List<BookResponse> list = service.getAllBooks();
                if(list.isEmpty()){
                    return ResponseEntity.noContent().build();
                }

            return ResponseEntity.ok(list);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getBookById(@PathVariable int id){
        BookResponse response = service.getBookById((long) id);

        return ResponseEntity.ok(response);

    }

    @PostMapping("/post")
    public ResponseEntity<?> postBook(@RequestBody @Valid BookRequest input){
        BookResponse response = service.postBook(input);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PatchMapping("/patchById/{id}")
    public ResponseEntity<?> patchBook(@PathVariable int id, Map<String,Object> update){
        BookResponse response = service.patchBookById((long)id,update);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/patchByName/{name}")
    public ResponseEntity<?> patchBookName(@PathVariable String name, Map<String,Object> update){
        BookResponse response = service.patchBookByName(name,update);

        return ResponseEntity.ok(response);
    }

    @PutMapping("putById/{id}")
    public ResponseEntity<?> putBookId(@PathVariable int id,BookRequest request){
        BookResponse update = service.putBookById((long)id,request);

        return ResponseEntity.ok(update);
    }

    @PutMapping("putByName/{name}")
    public ResponseEntity<?> putBookName(@PathVariable String name,BookRequest request){
        BookResponse update = service.putBookByName(name,request);

        return ResponseEntity.ok(update);
    }

}
