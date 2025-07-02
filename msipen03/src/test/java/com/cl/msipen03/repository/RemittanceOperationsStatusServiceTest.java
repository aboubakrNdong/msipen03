package com.cl.msipen03.repository;



/*
@SpringBootTest
@ExtendWith(SpringExtension.class)
class RemittanceOperationsStatusServiceTest extends CICSTestCase {

	@Autowired
	private RemittanceOperationsStatusService remittanceOperationsStatusService;

	@MockitoBean
	private DatabaseRepository mockDatabaseRepository;


	/*@Test
	void should_test(String paymentInfoId) throws InternalMSIPEN03Exception {

		final var result = mockDatabaseRepository.verifierPresenceOrdre(paymentInfoId);
		System.out.println("inside babacar");

		System.out.println(result);

	}*/

/*	@Test
	void should_test() throws InternalMSIPEN03Exception {
		// Arrange
		String paymentInfoId = "REM000000100956";

		final var result = mockDatabaseRepository.verifierPresenceOrdre(paymentInfoId);
		System.out.println("inside babacar");

		System.out.println(result);*/

/*
		when(mockDatabaseRepository.verifierPresenceOrdre(paymentInfoId))
				.thenReturn(true);  // or false depending on your test case

		// Act
		final var result = mockDatabaseRepository.verifierPresenceOrdre(paymentInfoId);

		// Assert
		System.out.println("inside babacar"+ result);
		assertTrue(result); */ // or other assertions
	//}

	/*@Test
	void should_throw_dataAccessException_when_call_createSuiviTraitementOrdre() throws CICSException, InternalMSIPEN03Exception {
		doThrow(new InternalMSIPEN03Exception("error dataAccessException") {
		}).when(mockDatabaseRepository).createSuiviTraitementOrdre(Mockito.any());
		try {
			suiviTraitementOrdreService.createSuiviTrtOrdre(new SuiviTraitementOrdre());
			fail("Should throw dataAccessException");
		} catch (InternalMSIPEN03Exception e) {
			assertEquals("error dataAccessException",
					e.getMessage());
		}
	}*/

	/*@Test
	void should_create_suivi_traitement_suivi_ordre() throws CICSException, InternalMSIPEN03Exception {
		
		when(mockDatabaseRepository.createSuiviTraitementOrdre(Mockito.any())).thenReturn(1);
		
		try {
			suiviTraitementOrdreService.createSuiviTrtOrdre(new SuiviTraitementOrdre());
		} catch (InternalMSIPEN03Exception e) {
			fail("Should throw dataAccessException");
		}
	}*/

	/*@Test
	void should_create_suivi_traitement_suivi_ordre_Exception() throws CICSException, InternalMSIPEN03Exception {
		
		when(mockDatabaseRepository.createSuiviTraitementOrdre(Mockito.any())).thenThrow(new InternalMSIPEN03Exception("Error when insert in Suivi_traitement_ordre"));
		
		assertThrows(InternalMSIPEN03Exception.class, () -> {
			suiviTraitementOrdreService.createSuiviTrtOrdre(new SuiviTraitementOrdre()); });

	}	*/

