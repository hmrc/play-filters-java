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
import play.Logger;
import play.api.mvc.*;
import scala.Function1;
import scala.concurrent.Future;

import java.util.regex.Pattern;

import static play.mvc.Results.movedPermanently;

public class RoutingFilter implements Filter {
    private static Route route;
    private static Pattern blockedPaths;

    public static void init(Route route, String blockedPaths) {
        if(blockedPaths == null) {
            Logger.info("No requests will be blocked based on their path");
            RoutingFilter.blockedPaths = null;
        } else {
            Logger.info("Any requests with paths that match {} will be blocked", blockedPaths);
            RoutingFilter.blockedPaths = Pattern.compile(blockedPaths);
        }

        RoutingFilter.route = route;
    }

    private boolean hasTrailingSlash(RequestHeader rh) {
        return rh.path().endsWith("/");
    }

    private boolean isBlocked(RequestHeader rh) {
        return blockedPaths != null && blockedPaths.matcher(rh.path()).matches();
    }

    private String normalizedPath(RequestHeader rh) {
        if(hasTrailingSlash(rh)) {
            return rh.path().substring(0, rh.path().length() - 1);
        } else {
            return rh.path();
        }
    }

    @Override
    public Future<Result> apply(Function1<RequestHeader, Future<Result>> next, RequestHeader rh) {
        if(hasTrailingSlash(rh)) {
            Logger.debug("Stripping slash: {}", normalizedPath(rh));
            return Futures.successful(movedPermanently(normalizedPath(rh)).toScala());
        } else if (isBlocked(rh)) {
            Logger.debug("URI blocked: {} by pattern {}", rh.path(), blockedPaths);
            return route.apply(rh);
        } else {
            return next.apply(rh);
        }
    }

    @Override
    public EssentialAction apply(EssentialAction next) {
        return Filter$class.apply(this, next);
    }

    public interface Route {
        Future<Result> apply(RequestHeader rh);
    }
}
