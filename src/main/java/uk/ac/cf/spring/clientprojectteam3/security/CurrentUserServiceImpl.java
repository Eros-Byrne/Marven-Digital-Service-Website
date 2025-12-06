//package uk.ac.cf.spring.clientprojectteam3.security;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//import uk.ac.cf.spring.clientprojectteam3.user.UserJdbcRepository;
//
//@Service
//public class CurrentUserServiceImpl implements CurrentUserService {
//
//    private final UserJdbcRepository userRepository;
//
//    public CurrentUserServiceImpl(UserJdbcRepository aUserRepository) {
//        userRepository = aUserRepository;
//    }
//
//    public Integer getCurrentUserId() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null || !auth.isAuthenticated()) {
//            return null;
//        }
//
//        // AI Generated code - START
//
//        String email = null;
//        Object principal = auth.getPrincipal();
//
//        if (principal instanceof UserDetails userDetails) {
//            email = userDetails.getUsername();
//        } else if (principal instanceof String s && !"anonymousUser".equals(s)) {
//            email = s;
//        }
//
//        return (email != null) ? userRepository.findUserIdByEmail(email) : null;
//
//        // AI Generated code - END
//    }
//
//    public String getCurrentUserEmail() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        return (auth != null && auth.isAuthenticated()) ? auth.getName() : null;
//    }
//}
