package org.riseger.testing;

import com.google.auto.service.AutoService;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.reseger.jrdbc.driver.connector.Connection;
import org.reseger.jrdbc.driver.session.TextSQLMessageSession;
import org.riseger.protocol.packet.RequestType;
import org.riseger.protocol.packet.response.TextSQLResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@AutoService(JavaSamplerClient.class)
public class JmeterSearchRequestTesting extends AbstractJavaSamplerClient implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(JmeterSearchRequestTesting.class);
    /**
     * The default value of the SamplerData parameter.
     */
    private static final String DEFAULT = "USE\n" +
            "  DATABASE 'test_db'|\n" +
            "  MAP 'china_mp'|\n" +
            "  SCOPE RECT(\n" +
            "    [1, 2],\n" +
            "    20000\n" +
            "  )|\n" +
            "  MODEL province_scope.area_scope.building_model\n" +
            "SEARCH\n" +
            "  building_model.KEY_LOOP,\n" +
            "  building_model.name\n" +
            "WHERE\n" +
            "  IN RECT(\n" +
            "    [4000, 1000],\n" +
            "    5000\n" +
            "  )\n" +
            "  OR OUT RECT(\n" +
            "    [777.5, 72658.1],\n" +
            "    45.2\n" +
            "  )\n" +
            "  AND building_model.floorArea > 1000;";
    /**
     * The sampler data (shown as Request Data in the Tree display).
     */
    private String query;

    @Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("query", DEFAULT);
        return params;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {

        SampleResult sampleResult = new SampleResult();
        sampleResult.setSampleLabel("Resiger Search Request"); // 设置请求名称
        sampleResult.setSamplerData(context.getParameter("query"));

        try (Connection connection = Connection.connect("localhost", 12000)) {
            sampleResult.sampleStart();

            TextSQLMessageSession statement = connection.sqlText();
            statement.setSqlText(sampleResult.getSamplerData());
            statement.setType(RequestType.SEARCH);
            //发送查询请求，并接收结果
            TextSQLResponse response = statement.send(10);
            //输出结果
            sampleResult.setSuccessful(response.isSuccess());
            sampleResult.setResponseMessage(response.toString());
            LOG.info(response.toString());
        } catch (Exception e) {
            sampleResult.setSuccessful(false);
            sampleResult.setSamplerData(e.getMessage());
        } finally {
            sampleResult.sampleEnd();// 结束统计响应时间标记
        }
        return sampleResult;

    }

}
