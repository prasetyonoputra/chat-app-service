package com.kurupuxx.chat.services.impl;

import com.kurupuxx.chat.entities.Contact;
import com.kurupuxx.chat.entities.MasterConfirmationStatus;
import com.kurupuxx.chat.entities.User;
import com.kurupuxx.chat.repositories.ContactRepository;
import com.kurupuxx.chat.repositories.MasterConfirmationStatusRepository;
import com.kurupuxx.chat.repositories.UserRepository;
import com.kurupuxx.chat.response.*;
import com.kurupuxx.chat.services.ContactService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MasterConfirmationStatusRepository masterConfirmationStatusRepository;

    @Override
    public ResponseEntity<GetListContactResponse> getContacts() {
        GetListContactResponse response = new GetListContactResponse();
        response.setTimestamp(new Date());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> userOptional = userRepository.findByUsername(authentication.getName());

        if (userOptional.isEmpty()) {
            response.setMessage("User not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = userOptional.get();

        List<User> users = contactRepository.findAllContact(user);
        List<ContactUserResponse> contacts = new ArrayList<>();

        for (User userContact : users) {
            Optional<Contact> optionalContact = contactRepository.findContactByUserAndUserToAdd(user, userContact);
            if (optionalContact.isPresent()) {
                Contact contact = optionalContact.get();
                String confirmationStatus = contact.getConfirmationStatus().getName();

                ContactUserResponse userResponse = ContactUserResponse.builder()
                        .username(userContact.getUsername())
                        .firstName(userContact.getFirstName())
                        .lastName(userContact.getLastName())
                        .status(userContact.getStatus())
                        .confirmationStatus(confirmationStatus)
                        .build();

                contacts.add(userResponse);
            }
        }


        response.setContacts(contacts);
        response.setMessage("Success get contact!");

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommonResponse> addContact(String username) {
        CommonResponse response = new CommonResponse();
        response.setTimestamp(new Date());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOptional = userRepository.findByUsername(authentication.getName());
        if (userOptional.isEmpty()) {
            response.setMessage("User not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = userOptional.get();
        if (user.getUsername().equals(username)) {
            response.setMessage("You can't add yourself to contact!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Optional<User> userToAddOptional = userRepository.findByUsername(username);
        if (userToAddOptional.isEmpty()) {
            response.setMessage("Username not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User userToAdd = userToAddOptional.get();
        if (contactRepository.findContactByUserAndUserToAdd(user, userToAdd).isPresent()) {
            response.setMessage("User already exists in your contacts!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Optional<Contact> confirmedContactOptional = contactRepository.findContactByUserAndUserToAdd(userToAdd, user);
        long confirmationId = confirmedContactOptional.isPresent() ? 2L : 1L;
        Optional<MasterConfirmationStatus> masterConfirmationStatusOptional = masterConfirmationStatusRepository.findById(confirmationId);

        if (masterConfirmationStatusOptional.isEmpty()) {
            response.setMessage("Master Confirmation Status Not Found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setUserToAdd(userToAdd);
        contact.setConfirmationStatus(masterConfirmationStatusOptional.get());
        Date now = new Date();
        contact.setCreatedAt(now);
        contact.setUpdatedAt(now);
        contact.setUpdatedBy(user.getUsername());

        contactRepository.save(contact);

        response.setMessage("Successfully added contact!");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GetDetailUserResponse> getDetailContact(String username) {
        GetDetailUserResponse response = new GetDetailUserResponse();
        response.setTimestamp(new Date());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOptional = userRepository.findByUsername(authentication.getName());
        if (userOptional.isEmpty()) {
            response.setMessage("User not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = userOptional.get();

        Optional<User> userToGetOptional = userRepository.findByUsername(username);
        if (userToGetOptional.isEmpty()) {
            response.setMessage("User not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User userToGet = userToGetOptional.get();

        Optional<Contact> validateContact = contactRepository.findContactByUserAndUserToAdd(user, userToGet);
        if (validateContact.isEmpty()) {
            response.setMessage("User not found on your contact!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.setMessage("Success get data contact!");
        response.setUser(UserResponse.builder()
                            .email(userToGet.getEmail())
                            .firstName(userToGet.getFirstName())
                            .lastName(userToGet.getLastName())
                            .username(userToGet.getUsername())
                            .status(userToGet.getStatus())
                            .socketId(userToGet.getSocketId())
                            .build()
        );

        return ResponseEntity.ok(response);

    }

    @Override
    public ResponseEntity<CommonResponse> deleteContact(String username) {
        CommonResponse response = new CommonResponse();
        response.setTimestamp(new Date());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOptional = userRepository.findByUsername(authentication.getName());
        if (userOptional.isEmpty()) {
            response.setMessage("User not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = userOptional.get();

        Optional<User> userToAddOptional = userRepository.findByUsername(username);
        if (userToAddOptional.isEmpty()) {
            response.setMessage("User not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User userToAdd = userToAddOptional.get();

        Optional<Contact> contactToDelete = contactRepository.findContactByUserAndUserToAdd(user, userToAdd);
        if (contactToDelete.isEmpty()) {
            response.setMessage("User not found on your contact!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        contactRepository.delete(contactToDelete.get());

        response.setMessage("Success delete contact!");
        return ResponseEntity.ok(response);
    }
}
