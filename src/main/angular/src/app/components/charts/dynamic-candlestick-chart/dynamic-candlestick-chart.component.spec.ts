import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DynamicCandlestickChartComponent } from './dynamic-candlestick-chart.component';

describe('DynamicCandlestickChartComponent', () => {
  let component: DynamicCandlestickChartComponent;
  let fixture: ComponentFixture<DynamicCandlestickChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DynamicCandlestickChartComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DynamicCandlestickChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
