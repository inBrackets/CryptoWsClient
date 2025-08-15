import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderBookLiveChartComponent } from './order-book-live-chart.component';

describe('OrderBookLiveChartComponent', () => {
  let component: OrderBookLiveChartComponent;
  let fixture: ComponentFixture<OrderBookLiveChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrderBookLiveChartComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderBookLiveChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
