package transfer

import groovy.json.JsonOutput
import io.restassured.http.ContentType
import spock.lang.Shared
import spock.lang.Specification

import javax.ws.rs.core.Response

import static io.restassured.RestAssured.given
import static transfer.config.ApplicationConfig.getConfig

class AccountIT extends Specification {

    @Shared String URI

    private static final String HOST = getConfig().getString("application.host");
    private static final int PORT = getConfig().getInt("application.port");

    def setup(){
        URI = "http://" + HOST + ":" + PORT + "/api/accounts/"
    }

    def "Should response has correct status code response when creating new account"(){
        def json = [:]
        json["balance"] = B

        expect:
        given().body(JsonOutput.toJson(json))
                .contentType(ContentType.JSON)
                .post(URI)
                .then()
                .assertThat()
                .statusCode(R)
                .extract()

        where:
        B       | R
        "10" | Response.Status.OK.statusCode
        "5.1" | Response.Status.OK.statusCode
        "10.01" | Response.Status.OK.statusCode
        "20.00" | Response.Status.OK.statusCode
        "0.00" | Response.Status.OK.statusCode
        "-0.01" | Response.Status.BAD_REQUEST.statusCode
        "-30" | Response.Status.BAD_REQUEST.statusCode
        "0.000" | Response.Status.BAD_REQUEST.statusCode
        "0.001" | Response.Status.BAD_REQUEST.statusCode
    }

    def "Should response has correct status code response when retrieving the account"(){
        setup: "Add new account to the system"
        def id = given().body(JsonOutput.toJson(["balance" : "10.00"]))
                .contentType(ContentType.JSON)
                .post(URI)
                .then()
                .extract()
                .path("accountId")

        when: "Get the account from the system"
        def response1 = given().get(URI + id).then().extract()
        def response2 = given().get(URI + "random_id").then().extract()

        then: "Status should be set correctly"
        response1.statusCode() == Response.Status.OK.statusCode
        response2.statusCode() == Response.Status.NOT_FOUND.statusCode
    }

    def "Should balance has correct value set when creating new account"(){
        def json = [:]
        json["balance"] = B

        def response = given().body(JsonOutput.toJson(json))
                .contentType(ContentType.JSON)
                .post(URI)
                .then()
                .extract()

        expect:
        new BigDecimal(response.path("balance").toString()) == new BigDecimal(B)
        response.path("accountId") != null

        where:
        B << ["10.00", "200", "20.01"]
    }

    def "Should get method return correct account"(){
        given: "Balance of the new account"
        def balance = "10.00"
        def json = ["balance" : balance]

        when: "Add new account and get it from the system"
        def response = given().body(JsonOutput.toJson(json))
                .contentType(ContentType.JSON)
                .post(URI)
                .then()
                .extract()

        def id = response.path("accountId")

        def account = given().get(URI + id).then().extract()

        then: "Balance and accountId should be the same"
        account.path("accountId") == id
        new BigDecimal(account.path("balance").toString()) == new BigDecimal(balance)
    }

    def "Should get accounts return the list of accounts"(){
        given: "Balance of the new accounts"
        def balance = "10.00"
        def json = ["balance" : balance]

        when: "Add two new accounts and get them from the system"
        def id1 = given().body(JsonOutput.toJson(json))
                .contentType(ContentType.JSON)
                .post(URI)
                .then()
                .extract()
                .path("accountId")

        def id2 = given().body(JsonOutput.toJson(json))
                .contentType(ContentType.JSON)
                .post(URI)
                .then()
                .extract()
                .path("accountId")

        def accounts = given().get(URI).then().extract()

        then: "List of return accounts should contains both"
        accounts.path("accountId").collect().containsAll([id1,id2])
    }
}
