/*
 * Copyright 2016 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.play.java.filters.frontend;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import play.api.mvc.Action;
import play.api.mvc.RequestHeader;
import play.test.FakeRequest;
import uk.gov.hmrc.play.filters.frontend.SessionTimeoutWrapper;
import uk.gov.hmrc.play.java.ScalaFixtures;

import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CSRFExceptionsFilterTest extends ScalaFixtures {
    private Supplier<DateTime> now = () -> DateTime.now().withZone(DateTimeZone.UTC);

    @Test
    public void doNothingIfPOSTRequestAndNotIdaLogin() {
        Action action = generateActionWithOkResponse();
        String validTime = Long.toString(now.get().minusSeconds(SessionTimeoutWrapper.timeoutSeconds() / 2).getMillis());
        RequestHeader rh = new FakeRequest("POST", "/something").withSession("ts", validTime).getWrappedRequest();
        await(new CSRFExceptionsFilter().apply(action).apply(rh));
        assertThat(captureRequest(action).headers().get("Csrf-Token").isDefined(), is(false));
    }

    @Test
    public void doNothingForGetRequests() {
        Action action = generateActionWithOkResponse();
        RequestHeader rh = new FakeRequest("GET", "/something").getWrappedRequest();
        await(new CSRFExceptionsFilter().apply(action).apply(rh));
        assertThat(captureRequest(action).headers().get("Csrf-Token").isDefined(), is(false));
    }

    @Test
    public void addCSRFTokenWithValuenocheckToBypassValidationForIdaLoginPOSTRequest() {
        Action action = generateActionWithOkResponse();
        RequestHeader rh = new FakeRequest("POST", "/ida/login").getWrappedRequest();
        await(new CSRFExceptionsFilter().apply(action).apply(rh));
        assertThat(captureRequest(action).headers().get("Csrf-Token").get(), is("nocheck"));
    }

    @Test
    public void addCSRFTokenWithValuenocheckToBypassValidationForSsoPOSTRequest() {
        Action action = generateActionWithOkResponse();
        RequestHeader rh = new FakeRequest("POST", "/ssoin").getWrappedRequest();
        await(new CSRFExceptionsFilter().apply(action).apply(rh));
        assertThat(captureRequest(action).headers().get("Csrf-Token").get(), is("nocheck"));
    }

    @Test
    public void addCSRFTokenWithValuenocheckToBypassValidationForContactReportPOSTRequest() {
        Action action = generateActionWithOkResponse();
        RequestHeader rh = new FakeRequest("POST", "/contact/problem_reports").getWrappedRequest();
        await(new CSRFExceptionsFilter().apply(action).apply(rh));
        assertThat(captureRequest(action).headers().get("Csrf-Token").get(), is("nocheck"));
    }

    @Test
    public void addCSRFTokenWithValuenocheckToBypassValidationForExpiredSession() {
        Action action = generateActionWithOkResponse();
        String invalidTime = Long.toString(new DateTime(2012, 7, 7, 4, 6, 20, DateTimeZone.UTC).minusDays(1).getMillis());
        RequestHeader rh = new FakeRequest("POST", "/some/post").withSession("ts", invalidTime).getWrappedRequest();
        await(new CSRFExceptionsFilter().apply(action).apply(rh));
        assertThat(captureRequest(action).headers().get("Csrf-Token").get(), is("nocheck"));
    }
}
