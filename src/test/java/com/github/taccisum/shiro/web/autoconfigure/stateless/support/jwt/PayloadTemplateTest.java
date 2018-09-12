package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * todo::
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/6
 */
public class PayloadTemplateTest {
    @Test
    public void testSimply() throws Exception {
        PayloadTemplate template = new DefaultPayloadTemplate("test");
        assertThat(template.hasField("tac", String.class)).isFalse();
        template.addField("tac", String.class);
        assertThat(template.hasField("tac", String.class)).isTrue();
        assertThat(template.getFieldNames().size()).isEqualTo(1);
        assertThat(template.getFieldNames()).contains("tac");
    }

//    @Test
//    public void checkMissingFields() throws Exception {
//        PayloadTemplate template = new PayloadTemplate();
//        template.addField("id", Long.class);
//        template.addField("name", String.class);
//        List<String> missingFieldsName = template.checkMissingFields(new Payload());
//        assertThat(missingFieldsName.size()).isEqualTo(2);
//        assertThat(missingFieldsName).contains("id", "name");
//    }
//
//    @Test
//    public void builder() throws Exception {
//        PayloadTemplate template = new PayloadTemplate();
//        template.addField("id", Long.class);
//        template.addField("name", String.class);
//        Payload payload = template.create()
//                .put("id", 123L)
//                .put("name", "taccisum")
//                .build();
//        assertThat(payload.get("id")).isEqualTo(123L);
//        assertThat(payload.get("name")).isEqualTo("taccisum");
//    }
//
//    @Test(expected = ErrorFieldException.class)
//    public void builderAddErrorField() throws Exception {
//        PayloadTemplate template = new PayloadTemplate();
//        template.addField("id", Long.class);
//        template.create().put("id1", 123L);
//    }
//
//    @Test(expected = ErrorFieldException.class)
//    public void builderAddErrorType() throws Exception {
//        PayloadTemplate template = new PayloadTemplate();
//        template.addField("id", Long.class);
//        template.create().put("id", "123");
//    }
//
//    @Test(expected = MissingFieldsException.class)
//    public void builderMissingFields() throws Exception {
//        PayloadTemplate template = new PayloadTemplate();
//        template.addField("id", Long.class);
//        template.addField("name", String.class);
//        template.create().build();
//    }
}