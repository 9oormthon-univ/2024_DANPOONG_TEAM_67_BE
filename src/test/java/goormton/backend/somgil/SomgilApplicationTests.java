//package goormton.backend.somgil;
//
//import goormton.backend.somgil.domain.course.dto.request.CourseRequest;
//import goormton.backend.somgil.domain.driver.domain.Driver;
//import goormton.backend.somgil.domain.driver.domain.repository.DriverRepository;
//import goormton.backend.somgil.domain.driver.exception.NoAvailableDriverException;
//import goormton.backend.somgil.domain.driver.service.DriverService;
//import goormton.backend.somgil.domain.packages.domain.UserPackage;
//import goormton.backend.somgil.domain.packages.domain.repository.PackageDetailsRepository;
//import goormton.backend.somgil.domain.packages.dto.request.CustomPackageRequest;
//import goormton.backend.somgil.domain.packages.dto.response.PackageResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional // 각 테스트 후 트랜잭션을 롤백하여 데이터베이스를 원상태로 유지
//class SomgilApplicationTests {
//
//    @Autowired
//    private PackageDetailsRepository packageDetailsRepository;
//
//    @Autowired
//    private DriverRepository driverRepository;
//
//    @Autowired
//    private DriverService driverService;
//
//    // 테스트 전에 공통으로 사용할 데이터 설정
//    @BeforeEach
//    void setUp() {
//        // 기존 데이터 삭제
//        packageDetailsRepository.deleteAll();
//        driverRepository.deleteAll();
//
//        // 테스트에 필요한 운전자 데이터 삽입
//        Driver driver1 = new Driver();
//        driver1.setDriverId("D001");
//        driver1.setName("운전자 1");
//        driver1.setContact("010-1111-1111");
//        driverRepository.save(driver1);
//
//        Driver driver2 = new Driver();
//        driver2.setDriverId("D002");
//        driver2.setName("운전자 2");
//        driver2.setContact("010-2222-2222");
//        driverRepository.save(driver2);
//
//        Driver driver3 = new Driver();
//        driver3.setDriverId("D003");
//        driver3.setName("운전자 3");
//        driver3.setContact("010-3333-3333");
//        driverRepository.save(driver3);
//    }
//
//    @Test
//    void assignDriversToExistingPackages_Success() {
//        // Given
//        CustomPackageRequest customPackageRequest = new CustomPackageRequest();
//        customPackageRequest.setName("패키지 A");
//        customPackageRequest.setDescription("설명 A");
//        customPackageRequest.setRecommended(true);
//
//        CourseRequest course1 = new CourseRequest();
//        course1.setRegion("서울");
//        course1.setPlace("장소 A1");
//        course1.setDescription("코스 설명 A1");
//        course1.setImage("image_url_A1");
//        course1.setStart(LocalDateTime.of(2024, 5, 1, 10, 0));
//        course1.setEnd(LocalDateTime.of(2024, 5, 1, 12, 0));
//
//        CourseRequest course2 = new CourseRequest();
//        course2.setRegion("서울");
//        course2.setPlace("장소 A2");
//        course2.setDescription("코스 설명 A2");
//        course2.setImage("image_url_A2");
//        course2.setStart(LocalDateTime.of(2024, 5, 2, 14, 0));
//        course2.setEnd(LocalDateTime.of(2024, 5, 2, 16, 0));
//
//        customPackageRequest.setCourses(Arrays.asList(course1, course2));
//        customPackageRequest.setTags(Arrays.asList("태그1", "태그2"));
//
//        // When
//        List<PackageResponse> responses = driverService.assignDriversToExistingPackages(Arrays.asList(customPackageRequest));
//
//        // Then
//        assertNotNull(responses);
//        assertEquals(1, responses.size());
//        PackageResponse response = responses.get(0);
//        assertEquals("패키지 A", response.getName());
//        assertNotNull(response.getDriver());
//        assertTrue(
//                Arrays.asList("운전자 1", "운전자 2", "운전자 3").contains(response.getDriver().getName()),
//                "할당된 운전자가 예상 범위 내에 있어야 합니다."
//        );
//
//        // 할당된 운전자가 실제로 DB에 저장되었는지 확인
//        UserPackage savedPackage = packageDetailsRepository.findByName(response.getName()).orElse(null);
//        assertNotNull(savedPackage);
//        assertNotNull(savedPackage.getDriver());
//        assertEquals(response.getDriver().getName(), savedPackage.getDriver().getName());
//    }
//
//    @Test
//    void assignDriversToExistingPackages_NoAvailableDriver() {
//        // Given
//        // 모든 운전자에게 패키지를 할당하여 이용 가능한 운전자가 없도록 설정
//        List<Driver> allDrivers = driverRepository.findAll();
//        for (Driver driver : allDrivers) {
//            UserPackage pkg = new UserPackage();
//            pkg.setName("패키지 " + driver.getDriverId());
//            pkg.setDescription("이미 할당된 패키지");
//            pkg.setRecommended(false);
//            pkg.setDriver(driver);
//
//            // 기존 패키지의 시간 범위를 새 패키지와 충돌하도록 설정
//            pkg.setStartDate(LocalDateTime.of(2024, 5, 3, 9, 0));
//            pkg.setEndDate(LocalDateTime.of(2024, 5, 3, 11, 0));
////            packagesRepository.save(pkg);
//
//            System.out.println("패키지: " + pkg.getName()+ ", 운전자: " + pkg.getDriver().getName());
//
//            driver.addPackage(pkg);
//            driverRepository.save(driver);
//            System.out.println("운전자 패키지 확인용: " + driver.getAPackages().get(0).getName());
//        }
//
//        // 새로운 패키지 생성 (시간 충돌 발생)
//        CustomPackageRequest customPackageRequest = new CustomPackageRequest();
//        customPackageRequest.setName("패키지 B");
//        customPackageRequest.setDescription("설명 B");
//        customPackageRequest.setRecommended(false);
//
//        CourseRequest course = new CourseRequest();
//        course.setRegion("부산");
//        course.setPlace("장소 B1");
//        course.setDescription("코스 설명 B1");
//        course.setImage("image_url_B1");
//        course.setStart(LocalDateTime.of(2024, 5, 3, 9, 30)); // 기존 패키지와 시간 충돌
//        course.setEnd(LocalDateTime.of(2024, 5, 3, 10, 30));
//
//        customPackageRequest.setCourses(Collections.singletonList(course));
//        customPackageRequest.setTags(Collections.singletonList("태그3"));
//
//        // When & Then
//        NoAvailableDriverException exception = assertThrows(NoAvailableDriverException.class, () -> {
//            driverService.assignDriversToExistingPackages(Arrays.asList(customPackageRequest));
//        });
//
//        // 예외 메시지 확인
//        assertEquals("패키지 '패키지 B'에 할당 가능한 운전자가 없습니다.", exception.getMessage());
//
//        // 새로운 패키지가 저장되지 않았는지 확인
//        List<UserPackage> savedPackages = packageDetailsRepository.findAll();
//        assertTrue(savedPackages.stream().noneMatch(pkg -> pkg.getName().equals("패키지 B")));
//    }
//
//    @Test
//    void assignDriversToExistingPackages_MultipleDrivers_AssignsOneRandomly() {
//        // Given
//        CustomPackageRequest customPackageRequest = new CustomPackageRequest();
//        customPackageRequest.setName("패키지 C");
//        customPackageRequest.setDescription("설명 C");
//        customPackageRequest.setRecommended(false);
//
//        CourseRequest course = new CourseRequest();
//        course.setRegion("인천");
//        course.setPlace("장소 C1");
//        course.setDescription("코스 설명 C1");
//        course.setImage("image_url_C1");
//        course.setStart(LocalDateTime.of(2024, 5, 4, 10, 0));
//        course.setEnd(LocalDateTime.of(2024, 5, 4, 12, 0));
//
//        customPackageRequest.setCourses(Collections.singletonList(course));
//        customPackageRequest.setTags(Collections.singletonList("태그4"));
//
//        // When
//        List<PackageResponse> responses = driverService.assignDriversToExistingPackages(Arrays.asList(customPackageRequest));
//
//        // Then
//        assertNotNull(responses);
//        assertEquals(1, responses.size());
//        PackageResponse response = responses.get(0);
//        assertEquals("패키지 C", response.getName());
//        assertNotNull(response.getDriver());
//        assertTrue(
//                Arrays.asList("운전자 1", "운전자 2", "운전자 3").contains(response.getDriver().getName()),
//                "할당된 운전자가 예상 범위 내에 있어야 합니다."
//        );
//
//        // 할당된 운전자가 실제로 DB에 저장되었는지 확인
//        UserPackage savedPackage = packageDetailsRepository.findByName(response.getName()).orElse(null);
//        assertNotNull(savedPackage);
//        assertNotNull(savedPackage.getDriver());
//        assertEquals(response.getDriver().getName(), savedPackage.getDriver().getName());
//    }
//}
