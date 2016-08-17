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
import play.api.mvc.Filter;
import play.api.mvc.RequestHeader;
import play.api.mvc.Result;
import scala.Function1;
import scala.concurrent.Future;
import uk.gov.hmrc.play.filters.frontend.CSRFExceptionsFilter$;

public class CSRFExceptionsFilter implements Filter {
  @Override
  public Future<Result> apply(Function1<RequestHeader, Future<Result>> f, RequestHeader rh) {
    return CSRFExceptionsFilter$.MODULE$.apply(f, rh);
  }

  @Override
  public EssentialAction apply(EssentialAction next) {
    return CSRFExceptionsFilter$.MODULE$.apply(next);
  }
}
