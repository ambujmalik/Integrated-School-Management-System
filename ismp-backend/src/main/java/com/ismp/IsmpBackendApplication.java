package com.ismp;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ismp.model.BlockEducationOfficer;
import com.ismp.model.DistrictOfficer;
import com.ismp.model.HeadMaster;
import com.ismp.model.Parent;
import com.ismp.model.Secretary;
import com.ismp.model.Student;
import com.ismp.model.Teacher;
import com.ismp.model.User;
import com.ismp.repository.BlockOfficerRepository;
import com.ismp.repository.DistrictOfficerRepository;
import com.ismp.repository.HeadMasterRepository;
import com.ismp.repository.ParentRepository;
import com.ismp.repository.SecretaryRepository;
import com.ismp.repository.StudentRepository;
import com.ismp.repository.TeacherRepository;
import com.ismp.repository.UserRepository;
import com.ismp.service.AuthService;

@SpringBootApplication
public class IsmpBackendApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(IsmpBackendApplication.class, args);
		
	}

//	@Bean
//    CommandLineRunner initDatabase(AuthService authService, UserRepository userRepository) {
//        return args -> {
//            if (userRepository.findByUsername("admin").isEmpty()) {
//                User admin = new User();
//                admin.setUsername("admin");
//                admin.setPasswordHash("admin123"); // Correct field name
//                admin.setUserRole("admin");     // Correct field name
//                admin.setFullName("System Administrator");
//                admin.setEmail("admin@ismp.com");   // Mandatory field in your entity
//                admin.setIsActive(true);            // Your entity defaults to true, but good to be safe
//                
//                authService.register(admin);
//                System.out.println("--- Default Admin Created: admin / admin123 ---");
//            }
//        };
//    }
	
	@Bean
	CommandLineRunner initDatabase(UserRepository userRepo, 
	                               StudentRepository studentRepo, 
	                               TeacherRepository teacherRepo, 
	                               ParentRepository parentRepo,
	                               HeadMasterRepository hmRepo,
	                               BlockOfficerRepository beoRepo,
	                               DistrictOfficerRepository deoRepo,
	                               SecretaryRepository secRepo,
	                               // Add this
	                               PasswordEncoder encoder) {
	    return args -> {
	        // 1. CREATE TEST STUDENT (If not exists)
	        if (userRepo.findByUsername("student@test.com").isEmpty()) {
	            User studentUser = new User();
	            studentUser.setUsername("student@test.com");
	            studentUser.setPasswordHash(encoder.encode("password123"));
	            studentUser.setUserRole("student"); // Matches your lowercase DB data
	            studentUser.setEmail("student@test.com");
	            studentUser.setFullName("Ravi Kumar");
	            studentUser.setIsActive(true);
	            userRepo.save(studentUser);

	            Student sProfile = new Student();
	            sProfile.setUser(studentUser);
	            sProfile.setFirstName("Ravi");
	            sProfile.setLastName("Kumar");
	            sProfile.setStudentCode("STU101");
	            sProfile.setAdmissionNumber("ADM2026001");
	            sProfile.setCurrentClass(10);
	            sProfile.setDateOfBirth(LocalDate.of(2010, 5, 15));
	            sProfile.setGender("Male");
	            sProfile.setAdmissionDate(LocalDate.now());
	            sProfile.setAcademicYear("2026-27");
	            sProfile.setStatus("Active");
	            studentRepo.save(sProfile);
	            System.out.println("✅ Student 'Ravi' Created");
	        }

	        // 2. CREATE TEST TEACHER (If not exists)
	        if (userRepo.findByUsername("teacher@test.com").isEmpty()) {
	            // Create Login Credentials
	            User teacherUser = new User();
	            teacherUser.setUsername("teacher@test.com");
	            teacherUser.setPasswordHash(encoder.encode("password123"));
	            teacherUser.setUserRole("teacher"); // Matches your lowercase DB data
	            teacherUser.setEmail("teacher@test.com");
	            teacherUser.setFullName("Priyadarshini Das");
	            teacherUser.setIsActive(true);
	            userRepo.save(teacherUser);

	            // Create Detailed Teacher Profile
	            Teacher tProfile = new Teacher();
	            tProfile.setUser(teacherUser); // Essential Link
	            tProfile.setFirstName("Priyadarshini");
	            tProfile.setLastName("Das");
	            tProfile.setEmployeeCode("TCH-OD-2026-88");
	            tProfile.setDesignation("Trained Graduate Teacher (TGT)");
	            tProfile.setGender("Female");
	            tProfile.setPhone("9876543210");
	            tProfile.setEmail("teacher@test.com");
	            tProfile.setDateOfBirth(LocalDate.of(1985, 8, 22));
	            tProfile.setJoiningDate(LocalDate.of(2015, 6, 10));
	            
	            teacherRepo.save(tProfile);
	            System.out.println("✅ Teacher 'Priyadarshini' Created (Employee Code: TCH-OD-2026-88)");
	        }
	        
	        if (userRepo.findByUsername("parent@test.com").isEmpty()) {
	            User parentUser = new User();
	            parentUser.setUsername("parent@test.com");
	            parentUser.setPasswordHash(encoder.encode("password123"));
	            parentUser.setUserRole("parent");
	            parentUser.setEmail("parent@test.com");
	            parentUser.setFullName("Mr. Suresh Kumar");
	            parentUser.setIsActive(true);
	            userRepo.save(parentUser);

	            // Retrieve the student we created earlier to link them
	            Student student = studentRepo.findAll().get(0); 

	            Parent pProfile = new Parent();
	            pProfile.setUser(parentUser);
	            pProfile.setStudent(student); // Linking Child
	            pProfile.setFirstName("Suresh");
	            pProfile.setLastName("Kumar");
	            pProfile.setRelationship("Father");
	            pProfile.setPhone("9988776655");
	            pProfile.setEmail("parent@test.com");
	            pProfile.setIsPrimaryContact(true);
	            
	            parentRepo.save(pProfile);
	            System.out.println("👨‍👩‍👦 Test Parent Created: parent@test.com / password123");
	        }
	        
	     // 4. CREATE TEST HEADMASTER (If not exists)
	        if (userRepo.findByUsername("hm@test.com").isEmpty()) {
	            // A. Create Login credentials
	            User hmUser = new User();
	            hmUser.setUsername("hm@test.com");
	            hmUser.setPasswordHash(encoder.encode("password123"));
	            hmUser.setUserRole("headmaster"); // Dashboard check uses .toLowerCase()
	            hmUser.setEmail("hm@test.com");
	            hmUser.setFullName("Dr. Binayak Mohanty");
	            hmUser.setIsActive(true);
	            userRepo.save(hmUser);

	            // B. Create Headmaster Profile
	            HeadMaster hmProfile = new HeadMaster();
	            hmProfile.setUser(hmUser); // Link to user
	            hmProfile.setName("Dr. Binayak Mohanty");
	            hmProfile.setOfficeRoom("Administrative Block - Room 01");
	            
	            // Note: If you have a BlockOfficer in DB, link it here. 
	            // For now, we save with just the core details.
	            hmRepo.save(hmProfile);

	            System.out.println("🏫 Headmaster 'Dr. Binayak' Created: hm@test.com / password123");
	        }
	        
	        if (userRepo.findByUsername("beo@test.com").isEmpty()) {
	            User beoUser = new User();
	            beoUser.setUsername("beo@test.com");
	            beoUser.setPasswordHash(encoder.encode("password123"));
	            beoUser.setUserRole("beo");
	            beoUser.setEmail("beo@ismp.com");
	            beoUser.setFullName("Shri Ramesh Chandra");
	            beoUser.setIsActive(true);
	            userRepo.save(beoUser);

	            BlockEducationOfficer beoProfile = new BlockEducationOfficer();
	            beoProfile.setUser(beoUser);
	            beoProfile.setName("Shri Ramesh Chandra");
	            beoProfile.setOffice("Block Education Office, Khurda");
	            // Link district officer if available
	            
	            beoRepo.save(beoProfile);
	            System.out.println("🏛️ BEO Account Created: beo@test.com / password123");
	        }
	        
	     // Inside your CommandLineRunner or init method
	        if (userRepo.findByUsername("deo_khordha").isEmpty()) {
	            // 1. Create the Login User
	            User deoUser = new User();
	            deoUser.setUsername("deo_khordha");
	            deoUser.setEmail("deo.khordha@odisha.gov.in");
	            deoUser.setPasswordHash(encoder.encode("deo@123")); // Use your BCrypt encoder
	            deoUser.setUserRole("deo");
	            deoUser.setFullName("District Education Officer - Khordha");
	            deoUser.setIsActive(true);
	            userRepo.save(deoUser);

	            // 2. Create the District Officer Profile
	            DistrictOfficer deoProfile = new DistrictOfficer();
	            deoProfile.setName("Shri Ashis Kumar");
	            deoProfile.setOffice("Collectorate Campus, Khordha");
	            //deoProfile.setUser(deoUser); // Linking the login account
	            
	            // Optional: Link to Regional Officer if you have one existing
	            // deoProfile.setRegionalOfficer(someRegionalOfficer); 
	            
	            deoRepo.save(deoProfile);

	            System.out.println("🏛️ DEO Account Created: deo_khordha / deo@123");
	        }
	        
	        if (userRepo.findByUsername("secretary_odisha").isEmpty()) {
	            User secUser = new User();
	            secUser.setUsername("secretary_odisha");
	            secUser.setUserRole("secretary");
	            secUser.setFullName("Secretary - S&ME Department");
	            
	            // ADD THIS LINE TO FIX THE ERROR
	            secUser.setEmail("secretary.sme@odisha.gov.in"); 
	            
	            // Ensure these fields match your User entity requirements
	            secUser.setIsActive(true); 
	            secUser.setPasswordHash(encoder.encode("apex@123"));
	            
	            userRepo.save(secUser);

	            Secretary secProfile = new Secretary();
	            secProfile.setName("Smt. Anu Garg");
	            secProfile.setOffice("Odisha Secretariat, Bhubaneswar");
	          //  secProfile.setDepartment("School & Mass Education"); // If you added this field
	            secProfile.setUser(secUser);
	            
	            secRepo.save(secProfile);
	        }
	    };
	}
	
}
