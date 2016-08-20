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

package uk.gov.hmrc.play.java;

import org.mockito.stubbing.Answer;
import org.scalatest.concurrent.*;
import org.scalatest.time.Millis$;
import org.scalatest.time.Span;
import play.Logger;
import play.api.mvc.*;
import play.test.WithApplication;
import scala.Function1;
import scala.compat.java8.JFunction0;
import scala.concurrent.Future;

import static org.mockito.Mockito.mock;

public abstract class ScalaFixtures extends WithApplication implements ScalaFutures {

    private PatienceConfig patienceConfig = new PatienceConfig(scaled(Span.apply(150, Millis$.MODULE$)), scaled(Span.apply(15, Millis$.MODULE$)));

    protected Action generateAction(Result result) {
        return generateAction(akka.dispatch.Futures.successful(result));
    }

    protected Action generateAction(Future<Result> result) {
        Action mockAction = mock(Action.class, (Answer) invocation -> {
            Logger.info("Invoking answer with {} - {}", invocation, invocation.getArguments()[0].getClass());

            if(invocation.getMethod().getName().equals("apply")) {
                JFunction0<Future<Result>> fn = () -> result;
                return ActionBuilder$class.async(Action$.MODULE$, fn).apply((RequestHeader) invocation.getArguments()[0]);
            } else {
                return invocation.callRealMethod();
            }
        });

        return mockAction;
    }


    @Override
    public Span scaled(Span span) {
        return ScaledTimeSpans$class.scaled(this, span);
    }

    @Override
    public <T> FutureConcept<T> convertScalaFuture(Future<T> scalaFuture) {
        return ScalaFutures$class.convertScalaFuture(this, scalaFuture);
    }

    @Override
    public PatienceConfig patienceConfig() {
        return patienceConfig;
    }

    public AbstractPatienceConfiguration.PatienceConfig$ PatienceConfig() {
        return new AbstractPatienceConfiguration.PatienceConfig$();
    }

    @Override
    public double spanScaleFactor() {
        return ScaledTimeSpans$class.spanScaleFactor(this);
    }

    @Override
    public Timeout timeout(Span value) {
        return PatienceConfiguration$class.timeout(this, value);
    }

    @Override
    public Interval interval(Span value) {
        return PatienceConfiguration$class.interval(this, value);
    }

    @Override
    public <T, U> U whenReady(FutureConcept<T> future, Timeout timeout, Interval interval, Function1<T, U> fun, PatienceConfig config) {
        return (U) Futures$class.whenReady(this, future, timeout, interval, fun, config);
    }

    @Override
    public <T, U> U whenReady(FutureConcept<T> future, Timeout timeout, Function1<T, U> fun, PatienceConfig config) {
        return (U)Futures$class.whenReady(this, future, timeout, fun, config);
    }

    @Override
    public <T, U> U whenReady(FutureConcept<T> future, Interval interval, Function1<T, U> fun, PatienceConfig config) {
        return (U)Futures$class.whenReady(this, future, interval, fun, config);
    }

    @Override
    public <T, U> U whenReady(FutureConcept<T> future, Function1<T, U> fun, PatienceConfig config) {
        return (U)Futures$class.whenReady(this, future, fun, config);
    }

    public PatienceConfig org$scalatest$concurrent$PatienceConfiguration$$defaultPatienceConfig() {
        return patienceConfig;
    }

    public void org$scalatest$concurrent$PatienceConfiguration$_setter_$org$scalatest$concurrent$PatienceConfiguration$$defaultPatienceConfig_$eq(PatienceConfig patienceConfig) {
        this.patienceConfig = patienceConfig;
    }
}
