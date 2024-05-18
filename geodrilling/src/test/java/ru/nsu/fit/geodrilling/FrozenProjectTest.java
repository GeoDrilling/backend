package ru.nsu.fit.geodrilling;

//@SpringBootTest
//public class FrozenProjectTest {
//
//    @MockBean
//    public ProjectRepository mockedProjectRepository;
//
//    @Autowired
//    public CurvesService curvesService;
//
//    @BeforeEach
//    public void initTestProject() {
//        ProjectEntity projectEntity = new ProjectEntity();
//        projectEntity.setReadOnly(true);
//        when(mockedProjectRepository.findById(anyLong())).thenReturn(Optional.of(projectEntity));
//        when(mockedProjectRepository.save(any())).thenReturn(null);
//    }
//
//    @Test
//    public void addCurvesToFrozenProjectTest() throws IOException {
//        Assertions.assertThrows(FrozenProjectException.class,
//                () -> curvesService.save(new MockMultipartFile("test", new byte[]{}), 1L));
//    }
//
//    @Test
//    public void supplementCurvesToFrozenProjectTest() {
//        Assertions.assertThrows(FrozenProjectException.class,
//                () -> curvesService.supplementCurve(new MockMultipartFile("test", new byte[]{}), 1L));
//    }
//
//}
