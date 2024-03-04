#include <stdarg.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>

typedef struct RustBukkit {
  int32_t (*broadcast_message_hnd)(const char*);
} RustBukkit;

int32_t broadcast_message(const struct RustBukkit *self, const char *msg);
