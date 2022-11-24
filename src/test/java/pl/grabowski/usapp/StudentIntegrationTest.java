package pl.grabowski.usapp;

import com.jayway.jsonpath.JsonPath;
import org.json.JSONException;
import org.json.JSONObject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import pl.grabowski.usapp.Repo.StudentRepository;
import pl.grabowski.usapp.Repo.TeacherRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class StudentIntegrationTest {
    @Value("${local.server.port}")
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Sql(value = "/reset.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)


    @Test
    void ShouldGetAllStudents() {
        //when
        var result = restTemplate.getForEntity("http://localhost:"+port+"/students?page=0", String.class);
        //then
        var json = JsonPath.parse(result.getBody());
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(json.read("$[4].firstName", String.class)).isEqualTo("Sula");
        assertThat(json.read("$[4].lastName", String.class)).isEqualTo("Baitey");
        assertThat(json.read("$[4].age", Integer.class)).isEqualTo(38);
        assertThat(json.read("$[4].mail", String.class)).isEqualTo("sbaitey4@biglobe.ne.jp");

        assertThat(json.read("$[9].firstName", String.class)).isEqualTo("Winn");
        assertThat(json.read("$[9].lastName", String.class)).isEqualTo("D'Oyly");
        assertThat(json.read("$[9].age", Integer.class)).isEqualTo(31);
        assertThat(json.read("$[9].mail", String.class)).isEqualTo("wdoyly9@skype.com");
    }

    @Test
    void should_return_student_by_first_name_and_last_name() {
        //when
        var result = restTemplate.getForEntity("http://localhost:"+port+"/students/search?page=0&firstName=Sula&lastName=Baitey", String.class);
        //then
        var json = JsonPath.parse(result.getBody());
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(json.read("$[0].firstName", String.class)).isEqualTo("Sula");
        assertThat(json.read("$[0].lastName", String.class)).isEqualTo("Baitey");
        assertThat(json.read("$[0].age", Integer.class)).isEqualTo(38);
        assertThat(json.read("$[0].mail", String.class)).isEqualTo("sbaitey4@biglobe.ne.jp");
    }

    @Test
    void Should_able_to_remove_student(){
        //given
        HttpHeaders headers = new HttpHeaders();
        HttpEntity requestHeaders = new HttpEntity(headers);
        // when
        var result = restTemplate.exchange("http://localhost:"+port+"/students/5", HttpMethod.DELETE,requestHeaders, String.class);

        var findDeleted = restTemplate.getForEntity("http://localhost:"+port+"/students/search?page=0&firstName=Sula&lastName=Baitey", String.class);
        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).isEqualTo("Student deleted");
        assertThat(findDeleted.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void Should_able_to_update_student() throws JSONException {
        //given
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();
        body.put("firstName", "CorrectedFirstName");
        body.put("lastName", "CorrectedLastName");
        HttpEntity<?> requestHeaders = new HttpEntity<>(body.toString(), header);

        // when
        var result = restTemplate.exchange("http://localhost:"+port+"/students/5", HttpMethod.POST, requestHeaders, String.class);

        // then
        var findUpdatedStudent = restTemplate.getForEntity("http://localhost:"+port+"/students/search?page=0&firstName=CorrectedFirstName&lastName=CorrectedLastName", String.class);
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();
        var json = JsonPath.parse(findUpdatedStudent.getBody());
        assertThat(json.read("$[0].firstName", String.class)).isEqualTo("CorrectedFirstName");
        assertThat(json.read("$[0].lastName", String.class)).isEqualTo("CorrectedLastName");
    }

    @Test
    void Should_create_new_student() throws JSONException {
        //given
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();
        body.put("firstName", "Pawel");
        body.put("lastName", "Grabowski");
        body.put("age", 33);
        body.put("mail", "grabowski@gmail.com");
        body.put("fieldOfStudy", "Computer science");
        HttpEntity<?> requestHeaders = new HttpEntity<>(body.toString(), header);

        // when
        var result = restTemplate.exchange("http://localhost:"+port+"/students", HttpMethod.POST, requestHeaders, String.class);

        // then

        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();
        var json = JsonPath.parse(result.getBody());
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(json.read("$.firstName", String.class)).isEqualTo("Pawel");
        assertThat(json.read("$.lastName", String.class)).isEqualTo("Grabowski");
        assertThat(json.read("$.age", Integer.class)).isEqualTo(33);
        assertThat(json.read("$.mail", String.class)).isEqualTo("grabowski@gmail.com");
        assertThat(json.read("$.fieldOfStudy", String.class)).isEqualTo("Computer science");
    }
}