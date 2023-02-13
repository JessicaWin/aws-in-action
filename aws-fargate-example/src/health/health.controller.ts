import { Controller, Get, HttpCode } from '@nestjs/common';
import {
  DiskHealthIndicator,
  HealthCheck,
  HealthCheckService,
  HttpHealthIndicator,
  MemoryHealthIndicator,
} from '@nestjs/terminus';

@Controller('health')
export class HealthController {
  constructor(
    private health: HealthCheckService,
    private http: HttpHealthIndicator,
    private memory: MemoryHealthIndicator,
    private disk: DiskHealthIndicator,
  ) {}

  @Get('success')
  @HttpCode(200)
  @HealthCheck()
  ping() {
    return 'ok!';
  }

  @Get('error')
  @HttpCode(500)
  @HealthCheck()
  pingError() {
    return 'error';
  }

  @Get('check')
  @HealthCheck()
  checkHttp() {
    return this.health.check([
      async () =>
        this.http.pingCheck('sample', 'http://localhost:3000/health/success'),
      async () => this.memory.checkHeap('memory_heap', 500 * 1024 * 1024),
      async () => this.memory.checkRSS('memory_rss', 3000 * 1024 * 1024),
      async () =>
        this.disk.checkStorage('storage', { thresholdPercent: 0.9, path: '/' }),
    ]);
  }
}
