import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RsiIndicatorSingleTimeframeComponent } from './rsi-indicator-single-timeframe.component';

describe('RsiIndicatorComponent', () => {
  let component: RsiIndicatorSingleTimeframeComponent;
  let fixture: ComponentFixture<RsiIndicatorSingleTimeframeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RsiIndicatorSingleTimeframeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RsiIndicatorSingleTimeframeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
