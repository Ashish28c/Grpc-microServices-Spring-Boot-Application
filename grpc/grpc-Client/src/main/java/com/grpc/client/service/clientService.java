package com.grpc.client.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.protobuf.Descriptors;
import com.grpc.proto.Author;
import com.grpc.proto.Book;
import com.grpc.proto.BookAuthorServiceGrpc;

import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;


@Service

public class clientService {
	
	private static final Logger logger = LoggerFactory.getLogger(clientService.class);

		    
	    @GrpcClient("grpc-service")
	    private BookAuthorServiceGrpc.BookAuthorServiceBlockingStub synchronousClient;
	    
	    @GrpcClient("grpc-service")
	    BookAuthorServiceGrpc.BookAuthorServiceStub asynchronousClient;

	    
	    public Map<Descriptors.FieldDescriptor, Object> getAuthor(int authorId) {
	        Author authorRequest = Author.newBuilder().setAuthorId(authorId).build();
	        
	        try {
	            Author authorResponse = synchronousClient.getAuthor(authorRequest);
	            return authorResponse.getAllFields();
	        } catch (StatusRuntimeException e) {
	            // Log the exception details
	            logger.error("gRPC request for getAuthor failed with status: " + e.getStatus(), e);
	            // You can throw an exception or return a default value or handle it based on your requirements
	            throw new RuntimeException("Failed to get author", e);
	        }
	    }
	    
	    
	    public List<Map<Descriptors.FieldDescriptor, Object>> getBooksByAuthor(int authorId) throws InterruptedException {
	        final Author authorRequest = Author.newBuilder().setAuthorId(authorId).build();
	        final CountDownLatch countDownLatch = new CountDownLatch(1);
	        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();
	        asynchronousClient.getBooksByAuthor(authorRequest, new StreamObserver<Book>() {
	            @Override
	            public void onNext(Book book) {
	                response.add(book.getAllFields());
	            }

	            @Override
	            public void onError(Throwable throwable) {
	                countDownLatch.countDown();
	            }

	            @Override
	            public void onCompleted() {
	                countDownLatch.countDown();
	            }
	        });
	        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
	        return await ? response : Collections.emptyList();
	    }

	

}
