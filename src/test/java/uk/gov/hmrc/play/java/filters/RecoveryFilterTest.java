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

package uk.gov.hmrc.play.java.filters;

import akka.dispatch.Futures;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import play.Logger;
import play.api.mvc.*;
import play.api.test.FakeRequest;
import scala.compat.java8.JFunction0;
import scala.compat.java8.JFunction1;
import scala.concurrent.Future;
import uk.gov.hmrc.play.http.HttpException;
import uk.gov.hmrc.play.http.NotFoundException;
import uk.gov.hmrc.play.java.ScalaFixtures;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class RecoveryFilterTest extends ScalaFixtures {
    @Test
    public void recoverFailedActionsWith404StatusCodes() {
        Action mockAction = generateAction(Futures.failed(new NotFoundException("Not found exception")));
        Result result = await(new RecoveryFilter().apply(mockAction).apply(FakeRequest.apply()));
        assertThat(result.header().status(), is(404));
    }

    @Test
    public void doNothingForActionsFailedWithOtherStatusCodes() {
        Action mockAction = generateAction(Futures.failed(new HttpException("Internal server error", 500)));
        Future<Result> fResult = new RecoveryFilter().apply(mockAction).apply(FakeRequest.apply()).run();
        JFunction1<Throwable, Boolean> fn = ex -> ex instanceof HttpException;

        assertThat(whenReady(convertScalaFuture(fResult.failed()), fn, patienceConfig()), is(true));
    }
}
