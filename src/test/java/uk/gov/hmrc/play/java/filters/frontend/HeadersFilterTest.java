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

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class HeadersFilterTest {
    @Test
    public void testDelegationOfXRequestIDHeader() {
        HeadersFilter filter = new HeadersFilter();
        assertThat(filter.xRequestId(), is("X-Request-ID"));
    }

    @Test
    public void testDelegationOfXRequestTimestampHeader() {
        HeadersFilter filter = new HeadersFilter();
        assertThat(filter.xRequestTimestamp(), is("X-Request-Timestamp"));
    }
}
