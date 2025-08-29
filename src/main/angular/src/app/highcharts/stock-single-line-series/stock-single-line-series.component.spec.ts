import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StockSingleLineSeriesComponent } from './stock-single-line-series.component';

describe('StockSingleLineSeriesComponent', () => {
  let component: StockSingleLineSeriesComponent;
  let fixture: ComponentFixture<StockSingleLineSeriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StockSingleLineSeriesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StockSingleLineSeriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
