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
import scala.collection.JavaConversions;
import scala.collection.Seq;
import scala.concurrent.Future;
import uk.gov.hmrc.play.audit.http.connector.AuditConnector;
import uk.gov.hmrc.play.filters.frontend.DeviceIdCookie$class;
import uk.gov.hmrc.play.filters.frontend.DeviceIdFilter$class;

import java.util.List;

public class DeviceIdFilter implements uk.gov.hmrc.play.filters.frontend.DeviceIdFilter {

    private final String appName;
    private final String secret;
    private final AuditConnector auditConnector;
    private final List<String> previousSecrets;

    public DeviceIdFilter(String appName, String secret, AuditConnector auditConnector, List<String> previousSecrets) {
        this.appName = appName;
        this.secret = secret;
        this.auditConnector = auditConnector;
        this.previousSecrets = previousSecrets;
    }

    public String secret() {
        return secret;
    }

    public AuditConnector auditConnector() {
        return auditConnector;
    }

    public String appName() {
        return appName;
    }

    @Override
    public Seq<String> previousSecrets() {
        return JavaConversions.asScalaBuffer(previousSecrets);
    }

    @Override
    public long getTimeStamp() {
        return DeviceIdCookie$class.getTimeStamp(this);
    }

    @Override
    public String generateUUID() {
        return DeviceIdCookie$class.generateUUID(this);
    }

    @Override
    public EssentialAction apply(EssentialAction next) {
        return Filter$class.apply(this, next);
    }

    @Override
    public uk.gov.hmrc.play.filters.frontend.DeviceId generateDeviceId(String uuid) {
        return DeviceIdCookie$class.generateDeviceId(this, uuid);
    }

    @Override
    public String generateDeviceId$default$1() {
        return DeviceIdCookie$class.generateDeviceId$default$1(this);
    }

    @Override
    public Cookie buildNewDeviceIdCookie() {
        return DeviceIdCookie$class.buildNewDeviceIdCookie(this);
    }

    @Override
    public Cookie makeCookie(uk.gov.hmrc.play.filters.frontend.DeviceId deviceId) {
        return DeviceIdCookie$class.makeCookie(this, deviceId);
    }

    @Override
    public Future<Result> apply(Function1<RequestHeader, Future<Result>> next, RequestHeader rh) {
        return DeviceIdFilter$class.apply(this, next, rh);
    }

    // Scala required method
    public uk.gov.hmrc.play.filters.frontend.DeviceIdFilter.CookeResult$ CookeResult() {
        return new uk.gov.hmrc.play.filters.frontend.DeviceIdFilter.CookeResult$();
    }
}
