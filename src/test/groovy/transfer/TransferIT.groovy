package transfer

import groovy.json.JsonOutput
import io.restassured.http.ContentType
import spock.lang.Shared
import spock.lang.Specification

import javax.ws.rs.core.Response

import static io.restassured.RestAssured.given
import static javax.ws.rs.core.Response.Status.BAD_REQUEST
import static javax.ws.rs.core.Response.Status.NOT_ACCEPTABLE
import static javax.ws.rs.core.Response.Status.NOT_FOUND
import static transfer.config.ApplicationConfig.getConfig
import static transfer.config.ApplicationConfig.getConfig

class TransferIT extends Specification {

    private static final String HOST = getConfig().getString("application.host")
    private static final int PORT = getConfig().getInt("application.port")

    @Shared String URI
    @Shared String fromId
    @Shared String toId

    def setup(){
        URI = "http://" + HOST + ":" + PORT + "/api/"
        def json = ["balance" : "1000"]
        toId = given().body(JsonOutput.toJson(json))
                .contentType(ContentType.JSON)
                .post(URI + "accounts")
                .path("accountId")
        fromId = given().body(JsonOutput.toJson(json))
                .contentType(ContentType.JSON)
                .post(URI + "accounts")
                .path("accountId")
    }

    def "Should accounts balances updates after money transfer"(){
        setup: "Two accounts added to the system"
        def amount = "200"
        def fromAfterTransfer = "800"
        def toAfterTransfer = "1200"

        def transfer = [:]
        transfer["toAccountId"] = toId
        transfer["fromAccountId"] = fromId
        transfer["amount"] = amount

        when: "Transfer money from one account to another"

        def response = given().body(JsonOutput.toJson(transfer))
                .contentType(ContentType.JSON)
                .post(URI + "transfer")

        then: "Balance of two accounts should be updated"
        response.statusCode() == Response.Status.OK.statusCode
        def balanceTo = given().get(URI + "accounts/" + toId).then().extract().path("balance")
        def balanceFrom = given().get(URI + "accounts/" + fromId).then().extract().path("balance")
        new BigDecimal(balanceTo.toString()) == (new BigDecimal(toAfterTransfer))
        new BigDecimal(balanceFrom.toString()) == (new BigDecimal(fromAfterTransfer))
    }

    def "Should transfer money return correct status code when not correct operation"(){
        setup: "Two accounts added to the system"

        def transfer = [:]
        transfer["toAccountId"] = TO
        transfer["fromAccountId"] = FROM
        if(AMOUNT != null)
            transfer["amount"] = String.valueOf(AMOUNT)

        when: "Transfer money from one account to another"
        def response = given().body(JsonOutput.toJson(transfer))
                .contentType(ContentType.JSON)
                .post(URI + "transfer")

        then: "Balance of two accounts should be updated"
        response.statusCode() == STATUS
        response.body().asString().contains(MESSAGE)

        where:
        FROM    | TO      | AMOUNT | STATUS                    | MESSAGE
        fromId  | toId    |11111111| BAD_REQUEST.statusCode    | "Transfer value is must not be larger than one million"
        fromId  | toId    | -33    | BAD_REQUEST.statusCode    | "Transfer value should be positive number"
        fromId  | toId    | 1.007  | BAD_REQUEST.statusCode    | "Incorrect balance value"
        fromId  | toId    | null   | BAD_REQUEST.statusCode    | "Transfer amount cannot be null"
        fromId  | null    | 1      | BAD_REQUEST.statusCode    | "To account must not be null"
        null    | toId    | 1      | BAD_REQUEST.statusCode    | "From account must not be null"
        fromId  | toId    | 5000   | NOT_ACCEPTABLE.statusCode | "Not enough money on the sender account"
        fromId  | fromId  | 1      | NOT_ACCEPTABLE.statusCode | "Sender and recipient must not be the same"
        fromId  | "r_id"  | 1      | NOT_FOUND.statusCode      | "Recipient is not found"
        "r_id"  | toId    | 1      | NOT_FOUND.statusCode      | "Sender is not found"
    }

}
