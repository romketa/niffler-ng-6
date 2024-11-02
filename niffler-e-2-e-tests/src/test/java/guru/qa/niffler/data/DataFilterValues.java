package guru.qa.niffler.data;

import lombok.Getter;

public enum DataFilterValues {
  TODAY("TODAY"), WEEK("WEEK"), MONTH("MONTH"), ALL("ALL");

  @Getter
  final String dataVal;

  DataFilterValues(String dataVal) {
    this.dataVal = dataVal;
  }
}
