package org.mixit.cesar.site.member;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mixit.cesar.site.config.CesarSerializerConfig;
import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.model.member.SharedLink;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Member can contain Sharelink which contains a member (cyclic reference)
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 23/12/15.
 */
public class MemberSerializerTest {

    private Jackson2ObjectMapperBuilder objectMapperBuilder;

    private final static String json = "{\n" +
            "  \"lastname\": \"Ehret\",\n" +
            "  \"company\": \"dev-mind\",\n" +
            "  \"sharedLinks\": [\n" +
            "    {\n" +
            "      \"id\": null,\n" +
            "      \"ordernum\": 0,\n" +
            "      \"name\": null,\n" +
            "      \"URL\": \"https://dev-mind.fr\",\n" +
            "      \"url\": \"https://dev-mind.fr\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    @Before
    public void setUp(){
        objectMapperBuilder = new CesarSerializerConfig().jacksonBuilder();
    }

    @Test
    public void shouldSerializeMemberWithShareLink() throws JsonProcessingException {
        Member<Member> member = new Member<>()
                .setLastname("Ehret")
                .setCompany("dev-mind");
        member.addSharedLink(new SharedLink().setURL("https://dev-mind.fr").setMember(member));

        Assertions.assertThat(objectMapperBuilder.build().writeValueAsString(member)).isNotNull();
    }

    @Test
    public void shouldDeserializeMemberWithShareLink() throws IOException {
        Member member = objectMapperBuilder.build().readValue(json, Member.class);
        Assertions.assertThat(member.getSharedLinks()).extracting("URL").contains("https://dev-mind.fr");
    };
}
