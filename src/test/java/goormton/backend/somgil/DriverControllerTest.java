package goormton.backend.somgil;

import goormton.backend.somgil.domain.driver.domain.repository.DriverRepository;
import goormton.backend.somgil.domain.driver.service.DriverService;
import goormton.backend.somgil.domain.packages.dto.request.CustomPackageRequest;
import goormton.backend.somgil.domain.packages.dto.request.PackageRequest;
import goormton.backend.somgil.domain.packages.dto.response.PackageResponse;
import goormton.backend.somgil.domain.user.domain.User;
import goormton.backend.somgil.domain.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class DriverControllerTest {

    @Autowired
    private DriverService driverService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackageDetailsRepository packageDetailsRepository;

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private BaseCourseRepository baseCourseRepository;

    @BeforeEach
    void setUp() {

        // 데이터 초기화
        driverRepository.deleteAll();
        packageDetailsRepository.deleteAll();

        Driver driver1 = Driver.builder().driverId("D001").name("Driver 1").contact("010-1234-5678").build();
        Driver driver2 = Driver.builder().driverId("D002").name("Driver 2").contact("010-9876-5432").build();
        driverRepository.saveAll(Arrays.asList(driver1, driver2));

        BaseCourse course1 = BaseCourse.builder()
                .day(1).region("Seoul").place("Place A").description("Description A")
                .startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(11, 0))
                .build();
        BaseCourse course2 = BaseCourse.builder()
                .day(2).region("Busan").place("Place B").description("Description B")
                .startTime(LocalTime.of(14, 0)).endTime(LocalTime.of(16, 0))
                .build();
        baseCourseRepository.saveAll(Arrays.asList(course1, course2));

        PackageDetails packageDetails = PackageDetails.builder()
                .packageId("P001").name("Sample Package").description("Sample Description").isRecommended(true)
                .build();
        packageDetails.addCourse(course1.getId());
        packageDetails.addCourse(course2.getId());
        packageDetailsRepository.save(packageDetails);
    }

    @BeforeEach
    void setUpAuthentication() {
        User testUser = User.builder()
                .email("test@example.com")
                .nickname("TestUser")
                .kakaoId("kakao123")
                .build();

        userRepository.save(testUser); // JPA로 저장 후 ID 할당

        // Spring Security 인증 객체 생성
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testUser, null, testUser.getAuthorities());

        // SecurityContextHolder에 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testAssignDriversToPackages() {
        // Given
        PackageRequest request = PackageRequest.builder()
                .packageId("P001")
                .build();

        request.addSelectedDate(LocalDate.of(2024, 5, 1));
        request.addSelectedDate(LocalDate.of(2024, 5, 2));

        // When
        List<PackageResponse> responses = driverService.assignDriversToExistingPackages(request);

        // Then
        assertNotNull(responses);
        assertEquals(1, responses.size());
        PackageResponse response = responses.get(0);
        assertEquals("Sample Package", response.getName());
        assertNotNull(response.getDriveCourseResponses());
        response.getDriveCourseResponses().forEach(driveCourse -> {
            assertNotNull(driveCourse.getDriverName());
            assertNotNull(driveCourse.getStartTime());
            assertNotNull(driveCourse.getEndTime());
        });
    }

    @Test
    void testAssignDriversToCustomPackages() {
        // Given
        CustomPackageRequest request = CustomPackageRequest.builder()
                .region("Seoul")
                .build();

        request.addSelectedDate(LocalDate.of(2024, 5, 1));
        request.addSelectedDate(LocalDate.of(2024, 5, 2));

        // When
        PackageResponse response = driverService.assignDriverByLocal(request);

        // Then
        assertNotNull(response);
//        assertEquals("name", response.getName());
        System.out.println("패키지 이름: " + response.getName());
        assertNotNull(response.getDriveCourseResponses());
        response.getDriveCourseResponses().forEach(driveCourse -> {
            assertNotNull(driveCourse.getDriverName());
            assertNotNull(driveCourse.getStartTime());
            assertNotNull(driveCourse.getEndTime());
        });
    }

    @AfterEach
    void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }
}
