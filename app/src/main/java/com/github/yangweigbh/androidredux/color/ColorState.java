/*
 * Copyright (c) 2017, Groupon, Inc.
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
package com.github.yangweigbh.androidredux.color;

public class ColorState {
  public static final int INVALID_COLOR = -1;

  public final int color;
  public final String error;
  public final boolean isRefreshing;

  public ColorState(int color, String error, boolean isRefreshing) {
    this.color = color;
    this.error = error;
    this.isRefreshing = isRefreshing;
  }

  public static ColorState empty() {
    return new ColorState(INVALID_COLOR, null, false);
  }

  public static ColorState refreshing() {
    return new ColorState(INVALID_COLOR, null, true);
  }

  public static ColorState success(int color) {
    return new ColorState(color, null, false);
  }

  public static ColorState error(String error) {
    return new ColorState(INVALID_COLOR, error, false);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ColorState that = (ColorState) o;

    if (color != that.color) return false;
    if (isRefreshing != that.isRefreshing) return false;
    return error != null ? error.equals(that.error) : that.error == null;
  }

  @Override
  public int hashCode() {
    int result = color;
    result = 31 * result + (error != null ? error.hashCode() : 0);
    result = 31 * result + (isRefreshing ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ColorState{" +
            "color=" + color +
            ", error='" + error + '\'' +
            ", isRefreshing=" + isRefreshing +
            '}';
  }
}
