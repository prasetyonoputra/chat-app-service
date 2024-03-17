package com.kurupuxx.cakap.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kurupuxx.cakap.model.Contact;
import com.kurupuxx.cakap.model.User;
import com.kurupuxx.cakap.repository.ContactRepository;
import com.kurupuxx.cakap.repository.MasterConfirmationStatusRepository;
import com.kurupuxx.cakap.repository.UserRepository;
import com.kurupuxx.cakap.response.CommonResponse;
import com.kurupuxx.cakap.response.ContactUserResponse;
import com.kurupuxx.cakap.response.GetListContactResponse;
import com.kurupuxx.cakap.service.ContactService;

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

        if (!userOptional.isPresent()) {
            response.setMessage("User not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = userOptional.get();

        List<User> users = contactRepository.findAllContact(user);
        List<ContactUserResponse> contacts = new ArrayList<>();

        for (User userContact : users) {
            String confirmationStatus = contactRepository.findContactByUserAndUserToAdd(user, userContact).get().getConfirmationStatus().getName();
            ContactUserResponse userResponse = ContactUserResponse.builder()
                    .email(userContact.getEmail())
                    .username(userContact.getUsername())
                    .firstName(userContact.getFirstName())
                    .lastName(userContact.getLastName())
                    .status(userContact.getStatus())
                    .confirmationStatus(confirmationStatus)
                    .build();

            contacts.add(userResponse);
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
        User user = userRepository.findByUsername(authentication.getName()).get();

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

        Optional<Contact> verifyContact = contactRepository.findContactByUserAndUserToAdd(user, userToAdd);
        if (verifyContact.isPresent()) {
            response.setMessage("User are exist in your contact!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Optional<Contact> confirmedContactOptional = contactRepository.findContactByUserAndUserToAdd(userToAdd, user);
        Long confirmationId = 1L;
        if (confirmedContactOptional.isPresent()) {
            confirmationId = 2L;

            Contact confirmedContact = confirmedContactOptional.get();
            confirmedContact.setConfirmationStatus(masterConfirmationStatusRepository.findById(confirmationId).get());
            confirmedContact.setUpdatedAt(new Date());
            confirmedContact.setUpdatedBy(user.getUsername());
        }

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setUserToAdd(userToAdd);
        contact.setConfirmationStatus(masterConfirmationStatusRepository.findById(confirmationId).get());
        contact.setCreatedAt(new Date());
        contact.setUpdatedAt(new Date());
        contact.setUpdatedBy(user.getUsername());

        contactRepository.save(contact);

        response.setMessage("Success add contact!");
        return ResponseEntity.ok(response);
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<CommonResponse> deleteContact(Long idUser) {
        CommonResponse response = new CommonResponse();
        response.setTimestamp(new Date());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).get();

        Optional<User> userToAddOptional = userRepository.findById(idUser);
        if (!userToAddOptional.isPresent()) {
            response.setMessage("User not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User userToAdd = userToAddOptional.get();

        Optional<Contact> contactToDelete = contactRepository.findContactByUserAndUserToAdd(user, userToAdd);
        if (!contactToDelete.isPresent()) {
            response.setMessage("User not found on your contact!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        contactRepository.delete(contactToDelete.get());

        response.setMessage("Success delete contact!");
        return ResponseEntity.ok(response);
    }
}
