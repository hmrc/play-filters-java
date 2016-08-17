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

import play.api.mvc.EssentialAction;
import uk.gov.hmrc.play.filters.frontend.HeadersFilter$;
import uk.gov.hmrc.play.filters.frontend.HeadersFilter$class;

public class HeadersFilter implements uk.gov.hmrc.play.filters.frontend.HeadersFilter {
    private String xRequestId = HeadersFilter$.MODULE$.xRequestId();
    private String xRequestTimestamp = HeadersFilter$.MODULE$.xRequestTimestamp();

    @Override
    public String xRequestId() {
        return xRequestId;
    }

    @Override
    public String xRequestTimestamp() {
        return xRequestTimestamp;
    }

    @Override
    public EssentialAction apply(EssentialAction nextAction) {
        return HeadersFilter$class.apply(this, nextAction);
    }

    public void uk$gov$hmrc$play$filters$frontend$HeadersFilter$_setter_$xRequestTimestamp_$eq(String xRequestTimestamp) {
        this.xRequestTimestamp = xRequestTimestamp;
    }

    public void uk$gov$hmrc$play$filters$frontend$HeadersFilter$_setter_$xRequestId_$eq(String xRequestId) {
        this.xRequestId = xRequestId;
    }
}
