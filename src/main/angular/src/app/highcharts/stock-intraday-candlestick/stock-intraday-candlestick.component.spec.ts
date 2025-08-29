import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StockIntradayCandlestickComponent } from './stock-intraday-candlestick.component';

describe('StockIntradayCandlestickComponent', () => {
  let component: StockIntradayCandlestickComponent;
  let fixture: ComponentFixture<StockIntradayCandlestickComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StockIntradayCandlestickComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StockIntradayCandlestickComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
