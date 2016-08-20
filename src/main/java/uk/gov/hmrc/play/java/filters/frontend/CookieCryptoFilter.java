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
import play.api.mvc.Filter$class;
import play.api.mvc.RequestHeader;
import play.api.mvc.Result;
import scala.Function1;
import scala.compat.java8.JFunction1;
import scala.concurrent.Future;
import uk.gov.hmrc.play.filters.frontend.CookieCryptoFilter$class;

public class CookieCryptoFilter implements uk.gov.hmrc.play.filters.frontend.CookieCryptoFilter {
    private JFunction1<String, String> decrypter;
    private JFunction1<String, String> encrypter;
    private String cookieName;

    public CookieCryptoFilter(String cookieName, Encrypter encrypter, Decrypter decrypter) {
        this.cookieName = cookieName;
        this.decrypter = decrypter;
        this.encrypter = encrypter;
    }

    @Override
    public EssentialAction apply(EssentialAction next) {
        return Filter$class.apply(this, next);
    }

    @Override
    public Future<Result> apply(Function1<RequestHeader, Future<Result>> next, RequestHeader rh) {
        return CookieCryptoFilter$class.apply(this, next, rh);
    }

    public String cookieName() {
        return cookieName;
    }

    @Override
    public Function1<String, String> encrypter() {
        return encrypter;
    }

    @Override
    public Function1<String, String> decrypter() {
        return decrypter;
    }

    public interface Decrypter extends JFunction1<String, String> {
        String apply(String val);
    }

    public interface Encrypter extends JFunction1<String, String> {
        String apply(String val);
    }

    public void uk$gov$hmrc$play$filters$frontend$CookieCryptoFilter$_setter_$cookieName_$eq(java.lang.String cookieName) {
        this.cookieName = cookieName;
    }
}
