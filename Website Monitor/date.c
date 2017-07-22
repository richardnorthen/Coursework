#include "date.h"
#include <stdlib.h> /* malloc, free, strtol, NULL */

/*
 * Richard Northen 951263471
 * CIS 415 Project 0
 * This is all my own work with the exception of lines 44, 77, and 79, as discussed in report.txt
 */

struct date {
  long day;
  long month;
  long year;
};

/*
 * date_create creates a Date structure from `datestr`
 * `datestr' is expected to be of the form "dd/mm/yyyy"
 * returns pointer to Date structure if successful,
 *         NULL if not (syntax error)
 */
Date *date_create(char *datestr) {
  Date *dt = (Date *) malloc(sizeof(Date));
  if (dt != NULL) {
    char *r, *q, *p;
    dt->day = strtol(datestr, &p, 10);
    if (p == datestr || *p != '/') {
      free(dt);
      return NULL;
    }
    p++;
    dt->month = strtol(p, &q, 10);
    if (q == p || *q != '/') {
      free(dt);
      return NULL;
    }
    q++;
    dt->year = strtol(q, &r, 10);
    if (r == q || *r != '\0') {
      free(dt);
      return NULL;
    }
    int days[] = {31,
      ((dt->year & 3) == 0 && ((dt->year % 25) != 0 || (dt->year & 15) == 0)) ? 29 : 28,
      31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    if (dt->year < 1 || dt->month < 1 || dt->month > 12 || dt->day < 1 || dt->day > days[dt->month-1]) {
      free(dt);
      dt = NULL;
    }
  }
  return dt;
}

/*
 * date_duplicate creates a duplicate of `d'
 * returns pointer to new Date structure if successful,
 *         NULL if not (memory allocation failure)
 */
Date *date_duplicate(Date *d) {
  Date *dt = (Date *) malloc(sizeof(Date));
  if (dt != NULL) {
    dt->day = d->day;
    dt->month = d->month;
    dt->year = d->year;
  }
  return dt;
}

/*
 * date_compare compares two dates, returning <0, 0, >0 if
 * date1<date2, date1==date2, date1>date2, respectively
 */
int date_compare(Date *date1, Date *date2) {
  int i, diff = 0, c = (date1->year < date2->year) ? 1 : -1,
      days[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
  for (i = date1->year; i != date2->year; i += c) {
    diff -= ((i & 3) == 0 && ((i % 25) != 0 || (i & 15) == 0)) ? c * 366 : c * 365;
  }
  days[1] = ((i & 3) == 0 && ((i % 25) != 0 || (i & 15) == 0)) ? 29 : 28;
  c = (date1->month < date2->month) ? 1 : -1;
  for (i = (date1->month - 1); i != (date2->month - 1); i = (i+c)%12) {
    diff -= c * days[i];
  }
  return diff -= (date2->day - date1->day) % days[i];
}

/*
 * date_destroy returns any storage associated with `d' to the system
 */
void date_destroy(Date *d) {
  free(d);
}
