package sk.liptovzije.api;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import sk.liptovzije.api.security.WebSecurityConfig;
import sk.liptovzije.core.service.file.IStorageService;
import sk.liptovzije.core.service.jwt.IJwtService;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(FileUploadApi.class)
@Import({WebSecurityConfig.class})
public class FileUploadApiTest extends TestWithCurrentUser {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private IStorageService storageService;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        super.setUp();
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    public void should_upload_multifile() throws Exception {
        String filename = "picture.jpg";
        String fileLocation = "resulting/path/" + filename;

        File file = folder.newFile(filename);
        IOUtils.write("Something21", new FileOutputStream(file));

        when(storageService.store(any())).thenReturn(fileLocation);
//        doNothing().when(storageService).store(any());

        given()
            .header("Authorization", "Token " + token)
            .contentType("multipart/form-data")
            .multiPart(file)
        .when()
            .post("/files/upload")
        .then()
            .statusCode(200)
            .body(equalTo(fileLocation));
    }
}
