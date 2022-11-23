package pl.grabowski.usapp;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import pl.grabowski.usapp.Repo.StudentRepository;
import pl.grabowski.usapp.Repo.TeacherRepository;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class StudentIntegrationTest {
    @Value("${local.server.port}")
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;
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
    void ShouldGetStudentById() {
        //when
        var result = restTemplate.getForEntity("http://localhost:"+port+"/students?page=0", String.class);
        //then

    }
}