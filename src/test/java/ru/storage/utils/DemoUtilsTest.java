package ru.storage.utils;

import org.springframework.beans.factory.annotation.Autowired;

public class DemoUtilsTest {


    private final FioService fioService;
    private final DemoUtils demoUtils;

    @Autowired
    DemoUtilsTest(FioService fioService) {
        this.fioService = fioService;
        this.demoUtils = new DemoUtils();
    }

//    @Test
//    void randomName_MaleFirstNames_ShouldReturnValidName() {
//        // When
//        String name = DemoUtils.FIO.randomName(2, true);
//
//        // Then
//        assertNotNull(name);
//        assertTrue(name.length() > 0);
//        assertArrayContains(demoUtils.getNameMale(), name);
//    }
//
//    @Test
//    void randomName_FemaleFirstNames_ShouldReturnValidName() {
//        // When
//        String name = DemoUtils.FIO.randomName(2, false);
//
//        // Then
//        assertNotNull(name);
//        assertTrue(name.length() > 0);
//        assertArrayContains(demoUtils.getNameFemale(), name);
//    }
//
//    @Test
//    void randomName_MaleLastNames_ShouldReturnValidLastName() {
//        // When
//        String lastName = DemoUtils.FIO.randomName(1, true);
//
//        // Then
//        assertNotNull(lastName);
//        assertTrue(lastName.length() > 0);
//    }
//
//    @Test
//    void randomName_FemaleLastNames_ShouldHaveFeminineForm() {
//        // When
//        String maleName = DemoUtils.FIO.randomName(1, true);
//        String femaleName = DemoUtils.FIO.randomName(1, false);
//
//        // Then
//        assertNotNull(maleName);
//        assertNotNull(femaleName);
//        assertNotEquals(maleName, femaleName);
//    }
//
//    @Test
//    void randomName_Surname_Male_ShouldEndWithOvich() {
//        // When
//        String surname = DemoUtils.FIO.randomName(3, true);
//
//        // Then
//        assertNotNull(surname);
//        assertTrue(surname.endsWith("евич") || surname.endsWith("ьевич") || surname.endsWith("ович"));
//    }
//
//    @Test
//    void randomName_Surname_Female_ShouldEndWithOvna() {
//        // When
//        String surname = DemoUtils.FIO.randomName(3, false);
//
//        // Then
//        assertNotNull(surname);
//        assertTrue(surname.endsWith("евна") || surname.endsWith("ьевна") || surname.endsWith("овна"));
//    }
//
//    @Test
//    void randomDate_ShouldReturnValidDate() {
//        // When
//        LocalDate date = DemoUtils.FIO.randomDate().toLocalDate();
//
//        // Then
//        assertNotNull(date);
//        assertTrue(date.getYear() >= 1930);
//        assertTrue(date.getYear() <= 2007);
//    }
//
//    @Test
//    void randomClient_ShouldCreateValidClient() {
//        // When
//        Client client = DemoUtils.FIO.randomClient();
//
//        // Then
//        assertNotNull(client);
//        assertNotNull(client.getFirstName());
//        assertNotNull(client.getLastName());
//        assertNotNull(client.getBirthDate());
//        assertNotNull(client.getPhoneNumber());
//        assertNotNull(client.getEmailAddress());
//        assertNotNull(client.getAddress());
//        assertTrue(client.getFirstName().length() > 0);
//        assertTrue(client.getLastName().length() > 0);
//    }
//
//    @Test
//    void getArraySurnames_Male_ShouldGeneratePatronymics() {
//        // Given
//        String[] maleNames = DemoUtils.FIO.getArrayNameMale();
//        String[] surnames = DemoUtils.FIO.getArraySurnames(true);
//
//        // Then
//        assertEquals(maleNames.length, surnames.length);
//        for (String surname : surnames) {
//            assertNotNull(surname);
//            assertTrue(surname.endsWith("евич") || surname.endsWith("ьевич") || surname.endsWith("ович"));
//        }
//    }
//
//    @Test
//    void getArraySurnames_Female_ShouldGeneratePatronymics() {
//        // Given
//        String[] maleNames = DemoUtils.FIO.getArrayNameMale();
//        String[] surnames = DemoUtils.FIO.getArraySurnames(false);
//
//        // Then
//        assertEquals(maleNames.length, surnames.length);
//        for (String surname : surnames) {
//            assertNotNull(surname);
//            assertTrue(surname.endsWith("евна") || surname.endsWith("ьевна") || surname.endsWith("овна"));
//        }
//    }
//
//    private void assertArrayContains(String[] array, String value) {
//        for (String item : array) {
//            if (item.equals(value)) {
//                return;
//            }
//        }
//        fail("Value '" + value + "' not found in array");
//    }
}