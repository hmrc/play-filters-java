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

import org.junit.Test;
import play.api.http.HeaderNames$;
import play.api.mvc.Result;
import play.api.test.FakeRequest;
import scala.Tuple2;
import uk.gov.hmrc.play.java.ScalaFixtures;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static play.test.Helpers.running;
import static scala.collection.JavaConversions.asScalaBuffer;

/**
 * Exercise a sub-set of the parent class's tests to verify delegation works as expected.
 */
public class CacheControlFilterTest extends ScalaFixtures {
    private List<Tuple2<String, String>> NO_CACHE_HEADER = asList(new Tuple2(HeaderNames$.MODULE$.CACHE_CONTROL(), "no-cache,no-store,max-age=0"));

    @Override
    protected Map<String, Object> additionalProperties() {
        Map<String, Object> props = super.additionalProperties();
        props.put("caching.allowedContentTypes", asList("image/", "text/css", "application/javascript"));
        return props;
    }

    @Test
    public void addACacheControlHeaderIfThereIsNotOneAndTheResponseHasNoContentType() {
        running(fakeApplication(), () -> {
            CacheControlFilter cacheControlFilter = new CacheControlFilter();
            Result result = await(cacheControlFilter.apply(generateAction(okResult)).apply(FakeRequest.apply()));
            assertThat(result, is(okResult.withHeaders(asScalaBuffer(NO_CACHE_HEADER))));
        });
    }

    @Test
    public void addACacheControlHeaderIfThereIsNotOneAndTheResponseDoesNotHaveAnIncludedContentType() {
        running(fakeApplication(), () -> {
            CacheControlFilter cacheControlFilter = new CacheControlFilter();
            Result initialResultType = okResult.as("text/html");
            Result result = await(cacheControlFilter.apply(generateAction(initialResultType)).apply(FakeRequest.apply()));
            assertThat(result, is(initialResultType.withHeaders(asScalaBuffer(NO_CACHE_HEADER))));
        });
    }

    @Test
    public void notAddACacheControlHeaderIfThereIsNotOneAndTheResponseIsAnExactMatchIncludedContentType() {
        running(fakeApplication(), () -> {
            CacheControlFilter cacheControlFilter = new CacheControlFilter();
            Result initialResultType = okResult.as("text/css");
            Result result = await(cacheControlFilter.apply(generateAction(initialResultType)).apply(FakeRequest.apply()));
            assertThat(result, is(initialResultType));
        });
    }
}
