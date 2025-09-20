import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RsiIndicatorAllTimeframesComponent } from './rsi-indicator-all-timeframes.component';

describe('RsiIndicatorAllTimeframesComponent', () => {
  let component: RsiIndicatorAllTimeframesComponent;
  let fixture: ComponentFixture<RsiIndicatorAllTimeframesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RsiIndicatorAllTimeframesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RsiIndicatorAllTimeframesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
