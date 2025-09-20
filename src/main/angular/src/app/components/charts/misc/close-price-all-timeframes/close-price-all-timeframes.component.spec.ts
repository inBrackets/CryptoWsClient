import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClosePriceAllTimeframesComponent } from './close-price-all-timeframes.component';

describe('ClosePriceAllComponent', () => {
  let component: ClosePriceAllTimeframesComponent;
  let fixture: ComponentFixture<ClosePriceAllTimeframesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClosePriceAllTimeframesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClosePriceAllTimeframesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
