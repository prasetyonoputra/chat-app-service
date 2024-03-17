package com.kurupuxx.cakap.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurupuxx.cakap.model.Contact;
import com.kurupuxx.cakap.model.User;
import com.kurupuxx.cakap.repository.ContactRepository;
import com.kurupuxx.cakap.repository.UserRepository;
import com.kurupuxx.cakap.response.CommonResponse;
import com.kurupuxx.cakap.response.GetListContactResponse;
import com.kurupuxx.cakap.response.UserResponse;
import com.kurupuxx.cakap.service.BaseService;
import com.kurupuxx.cakap.service.ContactService;

@Service
public class ContactServiceImpl extends BaseService implements ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<GetListContactResponse> getContacts(String token) {
        GetListContactResponse response = new GetListContactResponse();
        response.setTimestamp(new Date());

        User user = getUserByToken(token);

        if (user == null) {
            response.setMessage("User not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<User> users = contactRepository.findAllContact(user);
        List<UserResponse> contacts = new ArrayList<>();

        for (User userContact : users) {
            UserResponse userResponse = UserResponse.builder()
                    .email(userContact.getEmail())
                    .username(userContact.getUsername())
                    .firstName(userContact.getFirstName())
                    .lastName(userContact.getLastName())
                    .status(userContact.getStatus())
                    .build();

            contacts.add(userResponse);
        }

        response.setContacts(contacts);
        response.setMessage("Success get contact!");

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommonResponse> addContact(String token, String username) {
        CommonResponse response = new CommonResponse();
        response.setTimestamp(new Date());

        User user = getUserByToken(token);
        if (user == null) {
            response.setMessage("You must login first!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Optional<User> userToAddOptional = userRepository.findByUsername(username);
        if (!userToAddOptional.isPresent()) {
            response.setMessage("Username not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User userToAdd = userToAddOptional.get();
        if (userToAdd.getId() == user.getId()) {
            response.setMessage("You can't add yourself to contact!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Optional<User> verifyContact = contactRepository.findContactByUserAndUserToAdd(user, userToAdd);
        if (verifyContact.isPresent()) {
            response.setMessage("User are exist in your contact!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setUserToAdd(userToAdd);
        contact.setCreatedAt(new Date());
        contact.setUpdatedAt(new Date());
        contact.setUpdatedBy(user.getUsername());

        contactRepository.save(contact);

        response.setMessage("Success add contact!");
        return ResponseEntity.ok(response);
    }

}
