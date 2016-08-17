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


import play.api.mvc.*;
import scala.Function1;
import scala.concurrent.Future;
import uk.gov.hmrc.play.filters.frontend.CookieCryptoFilter$class;

public abstract class CookieCryptoFilter implements uk.gov.hmrc.play.filters.frontend.CookieCryptoFilter {
  @Override
  public EssentialAction apply(EssentialAction next) {
    return Filter$class.apply(this, next);
  }

  @Override
  public Future<Result> apply(Function1<RequestHeader, Future<Result>> next, RequestHeader rh) {
    return CookieCryptoFilter$class.apply(this, next, rh);
  }
}
