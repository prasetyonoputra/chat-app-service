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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public List<ContactUserResponse> getContacts() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> userOptional = userRepository.findByUsername(authentication.getName());

        if (userOptional.isEmpty()) {
            throw new Exception("User not found!");
        }

        User user = userOptional.get();

        List<User> users = contactRepository.findAllContact(user);
        List<ContactUserResponse> contacts = new ArrayList<>();

        for (User userContact : users) {
            Optional<Contact> optionalContact = contactRepository.findContactByAcceptorAndReceptor(user, userContact);

            if (optionalContact.isPresent()) {
                Contact contact = optionalContact.get();
                String confirmationStatus = contact.getConfirmationStatus().getName();

                ContactUserResponse userResponse = ContactUserResponse.builder().username(userContact.getUsername())
                        .firstName(userContact.getFirstName()).lastName(userContact.getLastName())
                        .status(userContact.getStatus()).confirmationStatus(confirmationStatus).build();

                contacts.add(userResponse);
            }
        }

        return contacts;
    }

    @Override
    public Map<String, Object> addContact(String username) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> userOptional = userRepository.findByUsername(authentication.getName());
        if (userOptional.isEmpty()) {
            throw new Exception("User not found!");
        }

        User requestor = userOptional.get();
        if (requestor.getUsername().equals(username)) {
            throw new Exception("You can't add yourself to contact!");
        }

        Optional<User> acceptorOptional = userRepository.findByUsername(username);
        if (acceptorOptional.isEmpty()) {
            throw new Exception("Username not found!");
        }

        User acceptor = acceptorOptional.get();
        if (contactRepository.findContactByAcceptorAndReceptor(requestor, acceptor).isPresent()) {
            throw new Exception("User already exists in your contacts!");
        }

        Optional<MasterConfirmationStatus> masterConfirmationStatusOptional = masterConfirmationStatusRepository
                .findByValue("requested");

        if (masterConfirmationStatusOptional.isEmpty()) {
            throw new Exception("Master Confirmation Status Not Found!");
        }

        Contact savedContact = contactRepository.save(Contact.builder()
                .requestor(requestor)
                .acceptor(acceptor)
                .confirmationStatus(masterConfirmationStatusOptional.get())
                .createdBy(requestor.getUsername())
                .updatedBy(requestor.getUsername())
                .build());

        Map<String, Object> response = new HashMap<>();
        response.put("contact_username", savedContact.getAcceptor().getUsername());
        response.put("status", savedContact.getConfirmationStatus().getValue());

        return response;
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

        Optional<Contact> validateContact = contactRepository.findContactByAcceptorAndReceptor(user, userToGet);
        if (validateContact.isEmpty()) {
            response.setMessage("User not found on your contact!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.setMessage("Success get data contact!");
        response.setUser(UserResponse.builder().email(userToGet.getEmail()).firstName(userToGet.getFirstName())
                .lastName(userToGet.getLastName()).username(userToGet.getUsername()).status(userToGet.getStatus())
                .socketId(userToGet.getSocketId()).build());

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

        Optional<User> receivedUserOptional = userRepository.findByUsername(username);
        if (receivedUserOptional.isEmpty()) {
            response.setMessage("User not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User receivedUser = receivedUserOptional.get();

        Optional<Contact> contactToDelete = contactRepository.findContactByAcceptorAndReceptor(user, receivedUser);
        if (contactToDelete.isEmpty()) {
            response.setMessage("User not found on your contact!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        contactRepository.delete(contactToDelete.get());

        response.setMessage("Success delete contact!");
        return ResponseEntity.ok(response);
    }
}
