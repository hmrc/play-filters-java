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
import scala.Tuple2;
import scala.collection.JavaConversions;
import scala.compat.java8.JFunction0;
import scala.concurrent.Future;
import uk.gov.hmrc.play.java.ScalaFixtures;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Exercise a sub-set of the parent class's tests to verify delegation works as expected.
 */
public class CacheControlFilterTest extends ScalaFixtures {
    private CacheControlFilter cacheControlFilter = new CacheControlFilter(Arrays.asList("image/", "text/css", "application/javascript"));
    private List<Tuple2<String, String>> headers = Arrays.asList(new Tuple2(play.api.http.HeaderNames$.MODULE$.CACHE_CONTROL(), "no-cache,no-store,max-age=0"));
    private Result okResult = Results$.MODULE$.Ok();

    @Test
    public void addACacheControlHeaderIfThereIsNotOneAndTheResponseHasNoContentType() {
        Result result = convertScalaFuture(cacheControlFilter.apply(generateAction(okResult)).apply(FakeRequest.apply()).run())
                .futureValue(patienceConfig());
        assertThat(result, is(okResult.withHeaders(JavaConversions.asScalaBuffer(headers))));
    }

    @Test
    public void addACacheControlHeaderIfThereIsNotOneAndTheResponseDoesNotHaveAnExcludedContentType() {
        Result initialResultType = okResult.as("text/html");
        Result result = convertScalaFuture(cacheControlFilter.apply(generateAction(initialResultType)).apply(FakeRequest.apply()).run())
                .futureValue(patienceConfig());
        assertThat(result, is(initialResultType.withHeaders(JavaConversions.asScalaBuffer(headers))));
    }

    @Test
    public void notAddACacheControlHeaderIfThereIsNotOneAndTheResponseIsAnExactMatchExcludedContentType() {
        Result initialResultType = okResult.as("text/css");
        Result result = convertScalaFuture(cacheControlFilter.apply(generateAction(initialResultType)).apply(FakeRequest.apply()).run())
                .futureValue(patienceConfig());
        assertThat(result, is(initialResultType));
    }
}
