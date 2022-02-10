# Password wallet
Password manager app

## Technologies
- Frontend
  - Angular, TS
  - Bootstrap 5
  

- Backend
  - Java 11
  - Spring Framework
      - Spring Boot
      - Spring Security
      - Spring JPA
      - Spring MVC
  - JUnit 5
  - Maven
  - Hibernate
  - MySQL, H2
  - Project Lombok


## Functionality
- keep user login data and its passwords
- user can choose how his password used to log into the application will be stored: SHA512 with salt and pepper or HMAC
- change the password to log in to the application. In the case of hash with a salt also change the salt 
  while user password change
- adding user passwords to the wallet. master password is necessary to decrypt user passwords
- decrypt a password only if a user asks for displaying it
- register user logins, login result (successful, failed), IP address
- display to the user the times of last successful and unsuccessful logins
- keep information about the number of subsequent incorrect trials of user logins
  - in the case if user failed to log in at least two times extend the time of user login verifications to 5 seconds 
  - in the case if user failed to log in at least three times extend the time of user login verifications to 10 seconds
  - in the case if user failed 4 to log in at least four times block the user account for 2 minutes
  - in the case of successful login reset the number of subsequent incorrect logins
- Verify IP addresses used by users. Count correct and incorrect logins from particular addresses
- Keep information about the number of subsequent incorrect trials from each IP address
  - in the case if users failed to log in at least two times from a particular IP address extend the time of user login verifications from this address to 5 seconds 
  - in the case if users failed to log in at least three times from the IP address extend the time of user logins verifications from the IP address to 10 seconds 
  - in the case if users failed to log in at least four times from the address block the address permanently
  - in the case of successful login from an IP reset the number of subsequent incorrect logins from that address
  - remove IP address blockade
- share passwords (A user, while displaying a password can choose “share” functionality. Inside it, user can define an email address of the other user with whom he wants to share the chosen password)
- viewing shared passwords (A person who uses a shared password should not be able to modify or delete the password)
- two modes of access to the wallet: read mode and modify mode
  - when the user logs into the wallet he is in "read" mode. He is able to overview passwords, share them, and create new passwords. No modifications are allowed to protect against accidental modification 
  - to edit passwords or delete them the user has to enter modify mode
