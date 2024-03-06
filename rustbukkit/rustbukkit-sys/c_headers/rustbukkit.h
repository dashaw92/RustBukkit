#include <stdarg.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>

typedef struct RustBukkit {
  int32_t (*broadcast_message_hnd)(const uint8_t*);
} RustBukkit;

int32_t broadcast_message(const struct RustBukkit *bukkit, const uint8_t *msg);
