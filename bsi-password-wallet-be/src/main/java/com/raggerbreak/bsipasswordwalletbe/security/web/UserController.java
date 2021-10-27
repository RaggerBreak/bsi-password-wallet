package com.raggerbreak.bsipasswordwalletbe.security.web;

import com.raggerbreak.bsipasswordwalletbe.security.service.UserService;
import com.raggerbreak.bsipasswordwalletbe.security.web.request.ChangeUserPasswordRequest;
import com.raggerbreak.bsipasswordwalletbe.security.web.response.MessageResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/password/change")
    public ResponseEntity<MessageResponse> changePassword(@Valid @RequestBody ChangeUserPasswordRequest request) throws Exception {

        MessageResponse messageResponse = userService.changePassword(request);

        if (messageResponse.isError())
            return ResponseEntity.internalServerError().body(messageResponse);

        return ResponseEntity.ok(messageResponse);

    }
}


