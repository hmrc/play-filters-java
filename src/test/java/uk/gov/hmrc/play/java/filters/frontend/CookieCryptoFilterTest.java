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

import akka.dispatch.Futures;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import play.api.mvc.*;
import play.api.test.FakeRequest;
import uk.gov.hmrc.play.java.ScalaFixtures;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static scala.collection.JavaConversions.asScalaBuffer;

/**
 * Subset of parent project's tests to verify interactions.
 */
public class CookieCryptoFilterTest extends ScalaFixtures {

    private String cookieName = "someCookieName";
    private CookieCryptoFilter.Encrypter encrypter = mock(CookieCryptoFilter.Encrypter.class);
    private CookieCryptoFilter.Decrypter decrypter = mock(CookieCryptoFilter.Decrypter.class);
    private CookieCryptoFilter cookieCryptoFilter = new CookieCryptoFilter(cookieName, encrypter, decrypter);
    private FakeRequest incomingRequest = FakeRequest.apply();
    private Result okResult = Results$.MODULE$.Ok();
    private Action mockAction = generateAction(Futures.successful(okResult));

    private Cookie encryptedCookie = Cookie.apply(cookieName, "encryptedValue", Cookie.apply$default$3(), Cookie.apply$default$4(), Cookie.apply$default$5(), Cookie.apply$default$6(), Cookie.apply$default$7());
    private Cookie unencryptedCookie = Cookie.apply(cookieName, "decryptedValue", Cookie.apply$default$3(), Cookie.apply$default$4(), Cookie.apply$default$5(), Cookie.apply$default$6(), Cookie.apply$default$7());
    private Cookie corruptEncryptedCookie = Cookie.apply(cookieName, "invalidEncryptedValue", Cookie.apply$default$3(), Cookie.apply$default$4(), Cookie.apply$default$5(), Cookie.apply$default$6(), Cookie.apply$default$7());
    private Cookie emptyCookie = Cookie.apply(cookieName, "", Cookie.apply$default$3(), Cookie.apply$default$4(), Cookie.apply$default$5(), Cookie.apply$default$6(), Cookie.apply$default$7());

    @Before
    public void setUp() {
        when(encrypter.apply("decryptedValue")).thenReturn("encryptedValue");
        when(decrypter.apply("encryptedValue")).thenReturn("decryptedValue");
        when(decrypter.apply("invalidEncryptedValue")).thenThrow(new RuntimeException("Couldn't decrypt that"));
    }

    private RequestHeader requestPassedToAction() {
        ArgumentCaptor<RequestHeader> updatedRequest = ArgumentCaptor.forClass(RequestHeader.class);
        verify(mockAction).apply((Object) updatedRequest.capture());
        return updatedRequest.getValue();
    }

    @Test
    public void testConstruction() {
        assertThat(cookieCryptoFilter.decrypter(), is(decrypter));
        assertThat(cookieCryptoFilter.encrypter(), is(encrypter));
    }

    @Test
    public void verifyThatEncryptedCookieIsDecryptedAndValueIsPassedToAction() {
        cookieCryptoFilter.apply(mockAction).apply(incomingRequest.withCookies(asScalaBuffer(singletonList(encryptedCookie))));
        assertThat(requestPassedToAction().cookies().get(cookieName).get().value(), is(unencryptedCookie.value()));
    }

    @Test
    public void verifyThatCorruptEncryptedCookieIsNotPassedToAction() {
        cookieCryptoFilter.apply(mockAction).apply(incomingRequest.withCookies(asScalaBuffer(singletonList(corruptEncryptedCookie))));
        assertThat(requestPassedToAction().cookies().get(cookieName).isDefined(), is(false));
    }

    @Test
    public void verifyThatCookieIsEncryptednReturn() {
        Result result = await(cookieCryptoFilter.apply(generateAction(Futures.successful(okResult.withCookies(asScalaBuffer(singletonList(unencryptedCookie)))))).apply(incomingRequest));
        assertThat(result, is(okResult.withCookies(asScalaBuffer(singletonList(encryptedCookie)))));
    }
}
