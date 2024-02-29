package com.grpc.service;

import com.grpc.proto.Author;
import com.grpc.proto.Book;
import com.grpc.proto.BookAuthorServiceGrpc.BookAuthorServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import proto.TempDB;

@GrpcService
public class BookAuthorService extends BookAuthorServiceImplBase {
	
	@Override
    public void getAuthor(Author request, StreamObserver<Author> responseObserver) {
        TempDB.getAuthorsFromTempDb()
                .stream()
                .filter(author -> author.getAuthorId() == request.getAuthorId())
                .findFirst()
                .ifPresent(responseObserver::onNext);
        responseObserver.onCompleted();
    }

	 @Override
	    public void getBooksByAuthor(Author request, StreamObserver<Book> responseObserver) {
	        TempDB.getBooksFromTempDb()
	                .stream()
	                .filter(book -> book.getAuthorId() == request.getAuthorId())
	                .forEach(responseObserver::onNext);
	        responseObserver.onCompleted();
	    }

}
