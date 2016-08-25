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

import play.Logger;
import play.Play;
import scala.collection.JavaConversions;
import scala.collection.Seq;
import uk.gov.hmrc.play.filters.CacheControlFilter$;

import java.util.List;

public class CacheControlFilter extends uk.gov.hmrc.play.filters.CacheControlFilter {
    private static List<String> cacheableContentTypes = null;

    public CacheControlFilter() {
        this("caching.allowedContentTypes");
    }

    public CacheControlFilter(String configKey) {
        super();
        if(cacheableContentTypes == null) {
            cacheableContentTypes = Play.application().configuration().getStringList(configKey);
            Logger.info("Will allow caching of content types matching: {}", cacheableContentTypes);
        }
    }

    public static uk.gov.hmrc.play.filters.CacheControlFilter fromConfig(String configKey) {
        return CacheControlFilter$.MODULE$.fromConfig(configKey);
    }

    @Override
    public Seq<String> cachableContentTypes() {
        return JavaConversions.asScalaBuffer(cacheableContentTypes);
    }
}
